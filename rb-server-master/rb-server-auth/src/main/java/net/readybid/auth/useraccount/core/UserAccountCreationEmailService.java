package net.readybid.auth.useraccount.core;

import net.readybid.auth.useraccount.UserAccount;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface UserAccountCreationEmailService {
    void notifyAbout(UserAccount userAccount);
}
