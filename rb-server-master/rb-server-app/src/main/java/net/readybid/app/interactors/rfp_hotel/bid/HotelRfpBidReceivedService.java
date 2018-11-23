package net.readybid.app.interactors.rfp_hotel.bid;

import net.readybid.rfphotel.bid.core.HotelRfpBid;

public interface HotelRfpBidReceivedService {
    void markBidAsReceivedByGuest(HotelRfpBid bid);

    void markBidAsReceivedByUser(String bidId);
}
