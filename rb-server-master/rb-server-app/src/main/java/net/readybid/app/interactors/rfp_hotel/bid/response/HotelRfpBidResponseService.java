package net.readybid.app.interactors.rfp_hotel.bid.response;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HotelRfpBidResponseService {
    List<HotelRfpBid> setFromDrafts(List<String> bidsIds, boolean ignoreErrors, Date responsesAt, AuthenticatedUser currentUser);

    HotelRfpBid setResponse(String bidId, Map<String, String> response, boolean ignoreErrors, AuthenticatedUser currentUser, Date responseAt);
}
