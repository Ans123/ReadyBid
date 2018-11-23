package net.readybid.app.use_cases.rfp_hotel.bid.implementation;

import net.readybid.app.entities.core.ActionReport;
import net.readybid.entities.Id;
import net.readybid.app.entities.email.EmailTemplate;
import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.app.entities.rfp_hotel.bid.AbstractHotelRfpBidActionReportBuilder;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryViewReader;
import net.readybid.app.interactors.email.EmailService;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidActionReportProducer;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidReceivedEmailFactory;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidSupplierContactProducer;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpSendBidAction;
import net.readybid.app.use_cases.rfp_hotel.bid.SendHotelRfpBidHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.user.BasicUserDetails;
import net.readybid.web.RbListViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static net.readybid.app.entities.core.ActionReport.Status.OK;
import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.*;

@Service
public class SendHotelRfpBidHandlerImpl implements SendHotelRfpBidHandler {

    private final HotelRfpSendBidAction hotelRfpSendBidAction;
    private final HotelRfpBidActionReportProducer actionReportProducer;
    private final HotelRfpBidReceivedEmailFactory bidReceivedEmailFactory;
    private final EmailService emailService;
    private final HotelRfpBidSupplierContactProducer supplierContactProducer;

    @Autowired
    public SendHotelRfpBidHandlerImpl(
            HotelRfpSendBidAction hotelRfpSendBidAction,
            HotelRfpBidActionReportProducer actionReportProducer,
            HotelRfpBidReceivedEmailFactory bidReceivedEmailFactory,
            EmailService emailService,
            HotelRfpBidSupplierContactProducer supplierContactProducer
    ) {
        this.hotelRfpSendBidAction = hotelRfpSendBidAction;
        this.actionReportProducer = actionReportProducer;
        this.bidReceivedEmailFactory = bidReceivedEmailFactory;
        this.emailService = emailService;
        this.supplierContactProducer = supplierContactProducer;
    }

    @Override
    public RbViewModel send(List<String> bidsIds, AuthenticatedUser currentUser, boolean ignoreDueDate) {
        final Date dateMark = new Date();
        hotelRfpSendBidAction.sendBids(bidsIds, currentUser, ignoreDueDate);
        final BidSentActionReportBuilder actionReportBuilder = new BidSentActionReportBuilder(dateMark);
        final List<ActionReport<HotelRfpBidQueryView>> actionReports = actionReportProducer.create(Id.asList(bidsIds), currentUser, actionReportBuilder);
        sendEmails(actionReports, currentUser);
        return new RbListViewModel<>(actionReports);
    }

    @Override
    public RbViewModel sendToNewContact(List<String> bidsIds, CreateHotelRfpSupplierContactRequest supplierContactRequest, AuthenticatedUser currentUser, boolean ignoreDueDate) {
        final HotelRfpSupplierContact supplierContact = supplierContactProducer.create(bidsIds, supplierContactRequest, currentUser);
        return sendToContact(bidsIds, supplierContact, currentUser, ignoreDueDate);
    }

    @Override
    public RbViewModel sendToSelectedContact(List<String> bidsIds, String userAccountId, AuthenticatedUser currentUser, boolean ignoreDueDate) {
        final HotelRfpSupplierContact supplierContact = supplierContactProducer.create(bidsIds, userAccountId);
        return sendToContact(bidsIds, supplierContact, currentUser, ignoreDueDate);
    }

    private RbViewModel sendToContact(List<String> bidsIds, HotelRfpSupplierContact supplierContact,
                                      AuthenticatedUser currentUser, boolean ignoreDueDate
    ){
        final Date dateMark = new Date();
        hotelRfpSendBidAction.sendBidsToNewContact(bidsIds, supplierContact, currentUser, ignoreDueDate);
        final BidSentToNewContactActionReportBuilder actionReportBuilder = new BidSentToNewContactActionReportBuilder(supplierContact, dateMark);
        final List<ActionReport<HotelRfpBidQueryView>> actionReports = actionReportProducer.create(Id.asList(bidsIds), currentUser, actionReportBuilder);
        sendEmails(actionReports, currentUser);
        return new RbListViewModel<>(actionReports);
    }

    private void sendEmails(List<ActionReport<HotelRfpBidQueryView>> actionReports, BasicUserDetails currentUser){
        final List<ActionReport<HotelRfpBidQueryView>> successfulActionReports = actionReports.stream()
                .filter(hotelRfpBidQueryViewActionReport -> OK.equals(hotelRfpBidQueryViewActionReport.status))
                .collect(Collectors.toList());
        final List<EmailTemplate> emails = bidReceivedEmailFactory.createAll(successfulActionReports, currentUser);
        emailService.send(emails);
    }

    public static class BidSentActionReportBuilder extends AbstractHotelRfpBidActionReportBuilder {

        private final Date sentAtMark;

        BidSentActionReportBuilder(Date sentAtMark) {
            this.sentAtMark = sentAtMark;
        }

        @Override
        protected ActionReport<HotelRfpBidQueryView> build(Id bidId, HotelRfpBidQueryViewReader bid) {
            if (bid == null || bid.isStateIn(DELETED)) {
                return buildReport(ActionReport.Status.NOT_FOUND, createNotFoundMessage(bidId), bid);
            } else if (bid.isBidSentStatusAfter(sentAtMark)) {
                return buildOkReport(bid);
            } else if (!bid.isStateIn(SENT, CREATED)) {
                return buildReport(ActionReport.Status.STATUS_CHANGED, createStatusChangedMessage(bid), bid);
            } else if (bid.isSupplierContactEmpty()) {
                return buildReport(ActionReport.Status.ERROR, "Supplier Contact not set", bid);
            } else {
                return buildReport(ActionReport.Status.ERROR, "Bid couldn't be sent due to unknown error", bid);
            }
        }
    }

    public static class BidSentToNewContactActionReportBuilder extends AbstractHotelRfpBidActionReportBuilder {

        private final HotelRfpSupplierContact supplierContact;
        private final Date sentAtMark;

        BidSentToNewContactActionReportBuilder(HotelRfpSupplierContact supplierContact, Date sentAtMark) {
            this.supplierContact = supplierContact;
            this.sentAtMark = sentAtMark;
        }

        @Override
        protected ActionReport<HotelRfpBidQueryView> build(Id bidId, HotelRfpBidQueryViewReader bid) {
            if(bid == null || bid.isStateIn(DELETED)){
                return buildReport(ActionReport.Status.NOT_FOUND, createNotFoundMessage(bidId), bid);
            } else if(supplierContact == null){
                return buildReport(ActionReport.Status.ERROR, "Supplier Contact couldn't be Created", bid);
            } else if (bid.containsSupplierContact(supplierContact.id) && bid.isBidSentStatusAfter(sentAtMark)) {
                return buildOkReport(bid);
            } else if (!bid.isStateIn(SENT, CREATED)){
                return buildReport(ActionReport.Status.STATUS_CHANGED, createStatusChangedMessage(bid), bid);
            } else {
                return buildReport(ActionReport.Status.ERROR, "Bid couldn't be sent due to unknown error", bid);
            }
        }
    }
}
