package net.readybid.api.main.bid.create;

import net.readybid.app.interactors.rfp_hotel.dirty.HotelRfpBidBuilder;

/**
 * Created by DejanK on 5/19/2017.
 */
public interface HotelRfpBidBuilderRepository {
    HotelRfpBidBuilder getHotelRfpBidBuilder(String hotelId);
}
