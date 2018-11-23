package net.readybid.rfphotel.bid.core;

import java.util.Date;

public interface HotelRfpBidState {

    HotelRfpBidStatusDetails getSupplierStatusDetails();

    HotelRfpBidStatusDetails getBuyerStatusDetails();

    HotelRfpBidStateStatus getStatus();

    default boolean matches(HotelRfpBidStateStatus status, Date at){
        return getStatus().equals(status) && (getSupplierStatusDetails().at.equals(at) || getBuyerStatusDetails().at.equals(at));
    }
}
