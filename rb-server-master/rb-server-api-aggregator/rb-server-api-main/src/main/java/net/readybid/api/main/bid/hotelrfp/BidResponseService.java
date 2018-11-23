package net.readybid.api.main.bid.hotelrfp;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationValues;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationsConfig;

/**
 * Created by DejanK on 5/30/2017.
 *
 */
public interface BidResponseService {

    HotelRfpBidOffer createOffer(NegotiationsConfig config, HotelRfpNegotiation negotiation);

    void updateResponseWithOffer(QuestionnaireResponse response, HotelRfpNegotiationValues negotiatedValues);
}
