package net.readybid.auth.useraccount.web;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.utils.ListResult;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/24/2017.
 *
 */
public interface UserAccountService {
    void updateLastUsedBidManager(AuthenticatedUser user, String viewId);

    ListResult<UserAccountViewModel> listUserAccountsForUser(AuthenticatedUser currentUser);

    UserAccount getByUserAndAccount(ObjectId userId, ObjectId accountId);

    UserAccount getById(String id);

    UserAccount getById(ObjectId objectId);
}
