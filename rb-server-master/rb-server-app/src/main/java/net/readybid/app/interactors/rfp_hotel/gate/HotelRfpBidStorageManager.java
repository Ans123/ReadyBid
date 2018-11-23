package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.app.entities.rfp_hotel.bid.HotelRfpAcceptedRates;

public interface HotelRfpBidStorageManager {

    void enableChainSupport(String rfpId);

    void setAcceptedRates(String bidId, HotelRfpAcceptedRates hotelRfpAcceptedRates);
}
