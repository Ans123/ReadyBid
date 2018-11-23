package net.readybid.api.main.bid.create;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfphotel.CreateBidRequest;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.app.core.entities.traveldestination.TravelDestination;

/**
 * Created by DejanK on 5/19/2017.
 *
 */
public interface CreateHotelRfpBidService {

    HotelRfpBid createBid(Rfp rfpId, TravelDestination destination, CreateBidRequest request, AuthenticatedUser currentUser);
}
