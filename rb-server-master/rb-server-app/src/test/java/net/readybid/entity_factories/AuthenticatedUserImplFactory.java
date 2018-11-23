package net.readybid.entity_factories;

import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.AuthenticatedUserImpl;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.test_utils.RbRandom;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class AuthenticatedUserImplFactory {

    public static AuthenticatedUserImpl random(Account account) {

        final AuthenticatedUserImpl user = new AuthenticatedUserImpl();

        user.setId(RbRandom.oid());
        user.setFirstName(RbRandom.name());
        user.setLastName(RbRandom.name());
        user.setEmailAddress(RbRandom.emailAddress());
        user.setPhone(RbRandom.phone());
        user.setProfilePicture(null);

        user.setCreated(CreationDetailsTestFactory.random());
        user.setStatus(UserStatusDetailsTestFactory.random());
        user.setLastChangeTimestamp(new Date().getTime());

        final UserAccount currentUserAccount = UserAccountTestFactory.random(user, account);

        user.setUserAccounts(new HashSet<>(Collections.singletonList(currentUserAccount.getId())));
        user.setCurrentUserAccountId(currentUserAccount.getId());
        user.setCurrentUserAccount(currentUserAccount);

        return user;
    }
}
