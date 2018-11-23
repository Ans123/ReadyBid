package net.readybid.app.use_cases.rfp_hotel.bid.implementation;

import net.readybid.app.entities.core.ActionReport;
import net.readybid.entities.Id;
import net.readybid.app.entities.rfp_hotel.bid.AbstractHotelRfpBidActionReportBuilder;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryViewReader;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidActionReportProducer;
import net.readybid.app.interactors.rfp_hotel.bid.response.HotelRfpBidResponseService;
import net.readybid.app.use_cases.rfp_hotel.bid.SetResponsesHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.web.RbListViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.DELETED;
import static net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus.RESPONDED;

@Service
public class SetResponsesHandlerImpl implements SetResponsesHandler {

    private final HotelRfpBidResponseService responseService;
    private final HotelRfpBidActionReportProducer actionReportProducer;

    @Autowired
    public SetResponsesHandlerImpl(HotelRfpBidResponseService responseService, HotelRfpBidActionReportProducer actionReportProducer) {
        this.responseService = responseService;
        this.actionReportProducer = actionReportProducer;
    }

    @Override
    public RbViewModel setResponses(List<String> bidsIds, boolean ignoreErrors, AuthenticatedUser currentUser) {
        final Date responsesAt = new Date();
        final List<HotelRfpBid> updatedBids = responseService.setFromDrafts(bidsIds, ignoreErrors, responsesAt, currentUser);
        return createActionReport(bidsIds, currentUser, updatedBids);
    }

    @Override
    public RbViewModel setResponse(String bidId, Map<String, String> response, boolean ignoreErrors, AuthenticatedUser currentUser) {
        final Date responseAt = new Date();
        final HotelRfpBid updatedBid = responseService.setResponse(bidId, response, ignoreErrors, currentUser, responseAt);
        return createActionReport(Collections.singletonList(bidId), currentUser, Collections.singletonList(updatedBid));
    }

    private RbViewModel createActionReport(List<String> bidsIds, AuthenticatedUser currentUser, List<HotelRfpBid> updatedBids) {
        final ActionReportBuilder actionReportBuilder = new ActionReportBuilder(updatedBids);
        final List<ActionReport<HotelRfpBidQueryView>> actionReports = actionReportProducer.create(Id.asList(bidsIds), currentUser, actionReportBuilder);
        return new RbListViewModel<>(actionReports);
    }

    public static class ActionReportBuilder extends AbstractHotelRfpBidActionReportBuilder {
        private final List<HotelRfpBid> updatedBids;

        ActionReportBuilder(List<HotelRfpBid> updatedBids) {
            this.updatedBids = updatedBids.stream().filter(Objects::nonNull).collect(Collectors.toList());
        }

        @Override
        protected ActionReport<HotelRfpBidQueryView> build(Id bidId, HotelRfpBidQueryViewReader bid) {
            if (bid == null || bid.isStateIn(DELETED)) {
                return buildReport(ActionReport.Status.NOT_FOUND, createNotFoundMessage(bidId), bid);
            } else if (areBidUpdatesSaved(bid)) {
                return buildOkReport(bid);
            } else if (bid.isStateIn(RESPONDED)){
                return buildReport(ActionReport.Status.ERROR, "Bid Response is already sent!", bid);
            } else {
                return buildReport(ActionReport.Status.STATUS_CHANGED, createStatusChangedMessage(bid), bid);
            }
        }

        private boolean areBidUpdatesSaved(HotelRfpBidQueryViewReader bid) {
            return containsMatch(updatedBids, bid);
        }
    }
}
