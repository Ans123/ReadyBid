package net.readybid.auth.bid;

import net.readybid.auth.useraccount.UserAccount;

/**
 * Created by DejanK on 5/21/2017.
 *
 */
public interface AuthBidService {
    void onNewUserAccount(UserAccount userAccount);
}
