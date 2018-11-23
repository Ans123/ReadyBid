package net.readybid.api.main.bid.negotiations;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;

/**
 * Created by DejanK on 8/2/2017.
 *
 */
public interface HotelRfpNegotiationsMailService {
    void sendNewNegotiationEmail(HotelRfpNegotiations negotiations, AuthenticatedUser currentUser);

    void sendNegotiationFinalizedEmail(HotelRfpNegotiations negotiations, HotelRfpNegotiation finalizedNegotiation, AuthenticatedUser currentUser);
}
