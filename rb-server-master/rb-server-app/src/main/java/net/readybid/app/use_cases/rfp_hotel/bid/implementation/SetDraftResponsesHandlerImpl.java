package net.readybid.app.use_cases.rfp_hotel.bid.implementation;

import net.readybid.app.entities.core.ActionReport;
import net.readybid.entities.Id;
import net.readybid.app.entities.rfp_hotel.bid.AbstractHotelRfpBidActionReportBuilder;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryViewReader;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidActionReportProducer;
import net.readybid.app.interactors.rfp_hotel.bid.response.HotelRfpBidDraftResponseService;
import net.readybid.app.use_cases.rfp_hotel.bid.SetDraftResponsesHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.web.RbListViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.*;

@Service
public class SetDraftResponsesHandlerImpl implements SetDraftResponsesHandler {

    private final HotelRfpBidDraftResponseService responseService;
    private final HotelRfpBidActionReportProducer actionReportProducer;

    @Autowired
    public SetDraftResponsesHandlerImpl(HotelRfpBidDraftResponseService responseService, HotelRfpBidActionReportProducer actionReportProducer) {
        this.responseService = responseService;
        this.actionReportProducer = actionReportProducer;
    }

    @Override
    public RbViewModel setResponses(List<String> bidsIds, List<Map<String, String>> validatedResponses, AuthenticatedUser currentUser) {
        final Date responsesAt = new Date();
        final List<HotelRfpBid> responses = responseService.saveDraftResponses(bidsIds, validatedResponses, responsesAt, currentUser);
        return createReport(Id.asList(bidsIds), responses, currentUser);
    }

    @Override
    public RbViewModel setResponse(String bidId, Map<String, String> response, AuthenticatedUser currentUser) {
        final Date responsesAt = new Date();
        final HotelRfpBid updatedBid = responseService.saveDraftResponse(bidId, response, responsesAt, currentUser);
        return createReport(Collections.singletonList(Id.valueOf(bidId)), Collections.singletonList(updatedBid), currentUser);
    }

    private RbViewModel createReport(List<Id> ids, List<HotelRfpBid> updatedBids, AuthenticatedUser currentUser) {
        final ActionReportBuilder actionReportBuilder = new ActionReportBuilder(updatedBids);
        final List<ActionReport<HotelRfpBidQueryView>> actionReports = actionReportProducer.create(ids, currentUser, actionReportBuilder);
        return new RbListViewModel<>(actionReports);
    }

    public static class ActionReportBuilder extends AbstractHotelRfpBidActionReportBuilder {
        private final List<HotelRfpBid> savedBids;

        ActionReportBuilder(List<HotelRfpBid> savedBids) {
            this.savedBids = savedBids;
        }

        @Override
        protected ActionReport<HotelRfpBidQueryView> build(Id bidId, HotelRfpBidQueryViewReader expectedBid) {
            if (expectedBid == null || expectedBid.isStateIn(DELETED)) {
                return buildReport(ActionReport.Status.NOT_FOUND, createNotFoundMessage(bidId), expectedBid);
            } else if (areBidUpdatesSaved(expectedBid)) {
                return buildOkReport(expectedBid);
            } else if (!expectedBid.isStateIn(SENT, RECEIVED)) {
                return buildReport(ActionReport.Status.STATUS_CHANGED, createStatusChangedMessage(expectedBid), expectedBid);
            } else {
                return buildReport(ActionReport.Status.ERROR, null, expectedBid);
            }
        }

        private boolean areBidUpdatesSaved(HotelRfpBidQueryViewReader expectedBid) {
            return containsMatch(savedBids, expectedBid);
        }
    }
}
