package net.readybid.api.main.bid;

import net.readybid.auth.invitation.Invitation;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

/**
 * Created by DejanK on 4/10/2017.
 *
 */
public interface BidEmailService {

    void notifyContactOfFinalAgreementReceived(Invitation invitation, HotelRfpBid bid);
}
