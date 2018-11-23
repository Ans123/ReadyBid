package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.rfphotel.bid.core.HotelRfpBidState;

import java.util.List;

public interface HotelRfpBidBuyerActionsStorageManager {
    void deleteBids(List<String> bidsIds, HotelRfpBidState state);
}
