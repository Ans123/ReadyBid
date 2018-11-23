package net.readybid.app.interactors.rfp_hotel.bid;

import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.bid.core.HotelRfpBidStatusDetails;
import net.readybid.user.BasicUserDetails;

import java.util.Date;

public interface HotelRfpBidStateFactory {
    HotelRfpBidState createSimpleState(HotelRfpBidStateStatus status, BasicUserDetails by, Date at);

    HotelRfpBidState createSimpleState(HotelRfpBidStateStatus status, BasicUserDetails by);

    HotelRfpBidState createReceivedState(HotelRfpBidStatusDetails buyerStatusDetails, long errorsCount, boolean isTouched, Date at, BasicUserDetails currentUser);

    HotelRfpBidState createReceivedState(HotelRfpBidStatusDetails buyerStatusDetails, long errorsCount, boolean isTouched, BasicUserDetails currentUser);
}
