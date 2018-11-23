package net.readybid.app.interactors.rfp_hotel.default_response;

import net.readybid.rfphotel.bid.core.HotelRfpBid;

import java.util.List;

public interface HotelRfpDefaultResponseService {
    void update(List<HotelRfpBid> bids);
}
