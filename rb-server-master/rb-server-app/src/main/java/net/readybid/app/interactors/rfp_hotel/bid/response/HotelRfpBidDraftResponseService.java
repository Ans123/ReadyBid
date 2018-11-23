package net.readybid.app.interactors.rfp_hotel.bid.response;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.user.BasicUserDetails;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HotelRfpBidDraftResponseService {
    List<HotelRfpBid> saveDraftResponses(List<String> bidsIds, List<Map<String,String>> validatedResponses, Date responsesAt, BasicUserDetails currentUser);

    HotelRfpBid saveDraftResponse(String bidId, Map<String, String> response, Date responseAt, AuthenticatedUser currentUser);
}
