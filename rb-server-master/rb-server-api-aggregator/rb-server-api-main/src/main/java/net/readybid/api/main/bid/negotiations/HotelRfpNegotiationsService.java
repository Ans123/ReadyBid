package net.readybid.api.main.bid.negotiations;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationRequest;
import net.readybid.web.RbViewModel;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
public interface HotelRfpNegotiationsService {
    HotelRfpNegotiations getNegotiations(String bidId);

    RbViewModel addNegotiation(String bidId, NegotiationRequest negotiationRequest, AuthenticatedUser user);

    RbViewModel updateBidNegotiations(String bidId, String negotiationId, NegotiationRequest negotiationRequest, AuthenticatedUser user);

    RbViewModel addAndFinalizeNegotiations(String bidId, NegotiationRequest negotiationRequest, AuthenticatedUser user);

    RbViewModel updateAndFinalizeNegotiations(String bidId, String negotiationId, NegotiationRequest negotiationRequest, AuthenticatedUser user);
}
