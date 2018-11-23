package net.readybid.app.interactors.rfp_hotel.bid.gate;

import net.readybid.entities.Id;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.web.BidsQueryRequest;

import java.util.List;

public interface HotelRfpBidQueryViewLoader {

    List<HotelRfpBidQueryView> find(List<Id> ids, AuthenticatedUser currentUser);

    List<HotelRfpBidQueryView> query(BidsQueryRequest bidsQueryRequest, AuthenticatedUser user);

    List<HotelRfpBidQueryView> query(List<String> bidsIds, List<String> fields, AuthenticatedUser currentUser);
}
