package net.readybid.app.interactors.rfp_hotel.bid.negotiation;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationsConfig;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationRequest;

import java.util.Map;

/**
 * Created by DejanK on 7/27/2017.
 *
 */
public interface HotelRfpNegotiationFactory {

    HotelRfpNegotiation create(HotelRfpNegotiationsConfig config, Map<String, String> response, AuthenticatedUser currentUser);

    HotelRfpNegotiation create(NegotiationRequest negotiationRequest, HotelRfpNegotiation lastCommunication, AuthenticatedUser currentUser);

    HotelRfpNegotiation create(String negotiationId, NegotiationRequest negotiationRequest, HotelRfpNegotiation secondLastCommunication, AuthenticatedUser currentUser);
}
