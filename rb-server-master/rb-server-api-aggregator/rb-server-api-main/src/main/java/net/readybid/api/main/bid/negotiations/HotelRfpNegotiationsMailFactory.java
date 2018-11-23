package net.readybid.api.main.bid.negotiations;

import net.readybid.auth.useraccount.UserAccount;
import net.readybid.email.Email;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;

/**
 * Created by DejanK on 8/2/2017.
 */
public interface HotelRfpNegotiationsMailFactory {
    Email createNewNegotiationEmail(UserAccount sender, UserAccount receiver, HotelRfpBid bid);

    Email createNegotiationFinalizedEmail(UserAccount currentUserAccount, UserAccount receiver, HotelRfpBid bid, HotelRfpNegotiation initialNegotiation, HotelRfpNegotiation finalizedNegotiation);
}
