package net.readybid.app.interactors.rfp_hotel.bid.negotiation;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationsImpl;

import java.util.Map;

/**
 * Created by DejanK on 7/26/2017.
 *
 */
public interface HotelRfpNegotiationsFactory {
    HotelRfpNegotiationsImpl createNegotiations(Map<String, String> answers, AuthenticatedUser currentUser);
}
