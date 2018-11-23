package net.readybid.entity_factories;

import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.User;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.core.UserAccountImpl;
import net.readybid.test_utils.RbRandom;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.Date;

public class UserAccountTestFactory {

    private UserAccountTestFactory(){}

    public static UserAccount random(User user, Account account) {

        final UserAccountImpl userAccount = new UserAccountImpl();
        userAccount.setId(RbRandom.oid());
        userAccount.setUser(user);
        userAccount.setAccount(account);

        userAccount.setJobTitle(RbRandom.name());
        userAccount.setCreated(CreationDetailsTestFactory.random());
        userAccount.setStatus(UserAccountStatusDetailsTestFactory.random());
        userAccount.setChanged(new Date().getTime());

        final ObjectId bmView = RbRandom.oid();
        userAccount.setDefaultBidManagerView(bmView);
        userAccount.setLastUsedBidManager(bmView);

        return userAccount;
    }
}
