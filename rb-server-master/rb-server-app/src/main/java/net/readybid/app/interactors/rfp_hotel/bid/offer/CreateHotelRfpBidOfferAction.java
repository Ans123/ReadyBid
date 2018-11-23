package net.readybid.app.interactors.rfp_hotel.bid.offer;

import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationValues;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationsConfig;

public interface CreateHotelRfpBidOfferAction {
    HotelRfpBidOffer createFromNegotiation(NegotiationsConfig config, HotelRfpNegotiationValues negotiationValues);
}
