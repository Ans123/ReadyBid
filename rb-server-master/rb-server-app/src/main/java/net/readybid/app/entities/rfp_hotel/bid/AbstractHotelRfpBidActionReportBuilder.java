package net.readybid.app.entities.rfp_hotel.bid;

import net.readybid.app.entities.core.ActionReport;
import net.readybid.app.entities.core.ActionReportBuilder;
import net.readybid.entities.Id;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

import java.util.List;

public abstract class AbstractHotelRfpBidActionReportBuilder implements ActionReportBuilder<HotelRfpBidQueryView> {

    @Override
    public ActionReport<HotelRfpBidQueryView> build(Id bidId, HotelRfpBidQueryView bidView){
        final HotelRfpBidQueryViewReader reader = new HotelRfpBidQueryViewReader();
        return build(bidId, reader.init(bidView));
    }

    protected abstract ActionReport<HotelRfpBidQueryView> build(Id bidId, HotelRfpBidQueryViewReader bid);

    protected String createStatusChangedMessage(HotelRfpBidQueryViewReader bid) {
        return String.format("Bid Status was changed to %s", bid.getStateStatus());
    }

    protected ActionReport<HotelRfpBidQueryView> buildOkReport(HotelRfpBidQueryViewReader bid) {
        return buildReport(ActionReport.Status.OK, null, bid);
    }

    protected ActionReport<HotelRfpBidQueryView> buildReport(
            ActionReport.Status status,
            String message,
            HotelRfpBidQueryViewReader bid
    ) {
        return new ActionReport<>(status, message, bid == null ? null : bid.getView());
    }

    protected boolean containsMatch(List<HotelRfpBid> savedBids, HotelRfpBidQueryViewReader expectedBid){
        return savedBids.stream()
                .anyMatch(savedBid ->
                        expectedBid.containsMatchingIdAndState(Id.valueOf(savedBid.getId()), savedBid.getState()));
    }

    protected String createNotFoundMessage(Id bidId) {
        return String.format("Bid with id %s wasn't found", bidId);
    }
}
