package net.readybid.app.interactors.rfp_hotel.bid.response;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

public interface HotelRfpBidQuestionnaireResponseProducer {
    QuestionnaireResponse prepareDraftResponse(HotelRfpBid bid);
}
