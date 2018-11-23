package net.readybid.app.use_cases.rfp_hotel.bid.implementation;

import net.readybid.app.entities.core.ActionReport;
import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.app.entities.rfp_hotel.bid.AbstractHotelRfpBidActionReportBuilder;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryViewReader;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidActionReportProducer;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidSupplierContactProducer;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidSupplierContactService;
import net.readybid.app.use_cases.rfp_hotel.bid.SetHotelRfpSupplierContactHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.Id;
import net.readybid.web.RbListViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.CREATED;
import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.DELETED;

@Service
class SetHotelRfpSupplierContactHandlerImpl implements SetHotelRfpSupplierContactHandler {

    private final HotelRfpBidActionReportProducer actionReportProducer;
    private final HotelRfpBidSupplierContactProducer supplierContactProducer;
    private final HotelRfpBidSupplierContactService supplierContactService;

    @Autowired
    public SetHotelRfpSupplierContactHandlerImpl(
            HotelRfpBidSupplierContactProducer supplierContactProducer,
            HotelRfpBidActionReportProducer actionReportProducer,
            HotelRfpBidSupplierContactService supplierContactService
    ) {
        this.supplierContactProducer = supplierContactProducer;
        this.actionReportProducer = actionReportProducer;
        this.supplierContactService = supplierContactService;
    }

    @Override
    public RbViewModel set(List<String> bidsIds, CreateHotelRfpSupplierContactRequest request, AuthenticatedUser currentUser) {
        final HotelRfpSupplierContact supplierContact = supplierContactProducer.create(bidsIds, request, currentUser);
        return set(bidsIds, supplierContact, currentUser);
    }

    @Override
    public RbViewModel set(List<String> bidsIds, String userAccountId, AuthenticatedUser currentUser) {
        final HotelRfpSupplierContact contact = supplierContactProducer.create(bidsIds, userAccountId);
        return set(bidsIds, contact, currentUser);
    }

    private RbViewModel set(List<String> bidsIds, HotelRfpSupplierContact supplierContact, AuthenticatedUser currentUser){
        if(supplierContact != null) supplierContactService.set(bidsIds, supplierContact);
        return createReport(bidsIds, supplierContact, currentUser);
    }

    private RbListViewModel createReport(List<String> bidsIds, HotelRfpSupplierContact supplierContact, AuthenticatedUser currentUser) {
        final ActionReportBuilder actionReportBuilder = new ActionReportBuilder(supplierContact);
        final List<ActionReport<HotelRfpBidQueryView>> actionReports = actionReportProducer.create(Id.asList(bidsIds), currentUser, actionReportBuilder);
        return new RbListViewModel<>(actionReports);
    }

    public static class ActionReportBuilder extends AbstractHotelRfpBidActionReportBuilder {

        private final HotelRfpSupplierContact supplierContact;

        ActionReportBuilder(HotelRfpSupplierContact supplierContact) {
            this.supplierContact = supplierContact;
        }

        @Override
        protected ActionReport<HotelRfpBidQueryView> build(Id bidId, HotelRfpBidQueryViewReader bid) {
            if(bid == null || bid.isStateIn(DELETED)){
                return buildReport(ActionReport.Status.NOT_FOUND, createNotFoundMessage(bidId), bid);
            } else if(supplierContact == null){
                return buildReport(ActionReport.Status.ERROR, "Supplier Contact couldn't be Created", bid);
            } else if(bid.containsSupplierContact(supplierContact.id)){
                return buildOkReport(bid);
            } else if (!bid.isStateIn(CREATED)){
                return buildReport(ActionReport.Status.STATUS_CHANGED, createStatusChangedMessage(bid), bid);
            } else {
                return buildReport(ActionReport.Status.ERROR, "Bid Supplier Contact couldn't be saved due to unknown error", bid);
            }
        }
    }
}
