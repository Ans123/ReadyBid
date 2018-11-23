package net.readybid.auth.useraccount.core;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.web.UserAccountViewModel;
import net.readybid.utils.ListResult;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public interface UserAccountRepository {

    String COLLECTION = "UserAccount";

    void create(UserAccount userAccount);

    UserAccount getById(ObjectId userAccountId);

    UserAccount getById(String id);

    void updateLastUsedBidManager(UserAccount user);

    List<? extends UserAccount> getUserAccountsByAccountId(ObjectId accountId);

    UserAccount getUserAccountByUserIdAndAccountId(ObjectId userId, ObjectId accountId);

    ListResult<UserAccountViewModel> listUserAccountsForUser(AuthenticatedUser currentUser);
}
