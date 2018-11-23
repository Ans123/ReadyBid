package net.readybid.core.rfp.hotel.bid;

import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.interactors.rfp_hotel.dirty.HotelRfpBidBuilder;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

/**
 * Created by DejanK on 4/4/2017.
 *
 */
public interface BidFactory {

    HotelRfpBid create(Rfp rfp, TravelDestination destination, HotelRfpBidBuilder hotelRfpBidBuilder, AuthenticatedUser currentUser);

    HotelRfpBid recreate(HotelRfpBid bid, Rfp rfp, TravelDestination destination, HotelRfpBidBuilder hotelRfpBidBuilder, AuthenticatedUser currentUser);
}
