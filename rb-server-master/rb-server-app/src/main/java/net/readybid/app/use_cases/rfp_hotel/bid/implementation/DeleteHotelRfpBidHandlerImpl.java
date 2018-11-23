package net.readybid.app.use_cases.rfp_hotel.bid.implementation;

import net.readybid.app.entities.core.ActionReport;
import net.readybid.app.entities.rfp_hotel.bid.AbstractHotelRfpBidActionReportBuilder;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryViewReader;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidActionReportProducer;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidBuyerActionsStorageManager;
import net.readybid.app.use_cases.rfp_hotel.bid.DeleteHotelRfpBidHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.Id;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.web.RbListViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.DELETED;

@Service
public class DeleteHotelRfpBidHandlerImpl implements DeleteHotelRfpBidHandler {

    private final HotelRfpBidActionReportProducer actionReportProducer;
    private final HotelRfpBidBuyerActionsStorageManager bidStorage;
    private final HotelRfpBidStateFactory stateFactory;

    @Autowired
    public DeleteHotelRfpBidHandlerImpl(
            HotelRfpBidActionReportProducer actionReportProducer,
            HotelRfpBidBuyerActionsStorageManager bidStorage,
            HotelRfpBidStateFactory stateFactory
    ) {
        this.actionReportProducer = actionReportProducer;
        this.bidStorage = bidStorage;
        this.stateFactory = stateFactory;
    }

    @Override
    public RbViewModel deleteBids(List<String> bidsIds, AuthenticatedUser currentUser) {
        bidStorage.deleteBids(bidsIds, stateFactory.createSimpleState(HotelRfpBidStateStatus.DELETED, currentUser));
        final ActionReportBuilder actionReportBuilder = new ActionReportBuilder();
        final List<ActionReport<HotelRfpBidQueryView>> actionReports = actionReportProducer.create(Id.asList(bidsIds), currentUser, actionReportBuilder);
        return new RbListViewModel<>(actionReports);
    }

    public static class ActionReportBuilder extends AbstractHotelRfpBidActionReportBuilder {
        @Override
        protected ActionReport<HotelRfpBidQueryView> build(Id bidId, HotelRfpBidQueryViewReader bid) {
            if (!bid.isStateIn(DELETED)) {
                return buildReport(ActionReport.Status.STATUS_CHANGED, "Bid changed before it could be deleted!", bid);
            } else if (bid.isStateIn(DELETED)) {
                return buildOkReport(bid);
            } else {
                return buildReport(ActionReport.Status.ERROR, "Bid couldn't be deleted to unknown error", bid);
            }
        }
    }
}
