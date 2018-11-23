package net.readybid.api.main.bid.negotiations;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
public interface HotelRfpBidNegotiationsRepository {
    HotelRfpNegotiations getNegotiations(String bidId);

    void addNegotiation(String bidId, HotelRfpNegotiation negotiation, HotelRfpBidState hotelRfpBidState);

    void addNegotiation(String bidId, HotelRfpNegotiation negotiation, HotelRfpBidOffer offer, HotelRfpBidState hotelRfpBidState);

    void updateNegotiation(String bidId, HotelRfpNegotiation negotiation, HotelRfpBidState hotelRfpBidState);

    void updateNegotiation(String bidId, HotelRfpNegotiation negotiation, HotelRfpBidOffer offer, HotelRfpBidState hotelRfpBidState);

    void addAndFinalize(String bidId, QuestionnaireResponse response, HotelRfpNegotiation negotiation, HotelRfpBidOffer offer, HotelRfpBidState hotelRfpBidState);

    void updateAndFinalize(String bidId, QuestionnaireResponse response, HotelRfpNegotiation negotiation, HotelRfpBidOffer offer, HotelRfpBidState hotelRfpBidState);
}
