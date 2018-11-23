package net.readybid.auth.useraccount.core;

import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.User;
import net.readybid.auth.useraccount.UserAccount;

/**
 * Created by DejanK on 3/24/2017.
 *
 */
public interface UserAccountFactory {
    UserAccount createUserAccount(Account account, User user, String jobTitle);

    UserAccount createUserAccountFromInvitation(User id, Account account, String jobTitle);
}
