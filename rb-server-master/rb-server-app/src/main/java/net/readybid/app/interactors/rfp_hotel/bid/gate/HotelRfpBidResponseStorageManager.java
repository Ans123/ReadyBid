package net.readybid.app.interactors.rfp_hotel.bid.gate;

import net.readybid.app.interactors.rfp_hotel.bid.implementation.SetBidReceivedData;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

import java.util.List;

public interface HotelRfpBidResponseStorageManager {
    void saveDraftResponses(List<HotelRfpBid> responses);

    void setBidsAsReceived(List<SetBidReceivedData> data);

    void saveResponses(List<HotelRfpBid> updatedBids);
}
