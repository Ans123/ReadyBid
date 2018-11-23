package net.readybid.app.interactors.rfp_hotel.bid.response.gate;

import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.bson.types.ObjectId;

import java.util.List;

public interface HotelRfpBidResponseLoader {
    HotelRfpBid getBidWithQuestionnaireHotelIdResponseContextFields(String bidId);

    List<HotelRfpBid> getBidsWithQuestionnaireHotelIdResponseContextFields(List<String> bidsIds);

    List<HotelRfpBid> getBidsWithQuestionnaireHotelIdResponseContextFieldsByRfpAndSupplierContact(ObjectId rfpId, String emailAddress);

    List<HotelRfpBid> loadBidsWithDraftToResponseContext(List<String> bidsIds);

    HotelRfpBid loadBidWithSetResponseContext(String bidId);
}
