package net.readybid.auth.useraccount.core;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.web.AddUserAccountRequest;
import net.readybid.auth.useraccount.web.CreateAccountRequest;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 5/19/2017.
 *
 */
public interface UserAccountCreationService {

    ObjectId createUserAccount(CreateAccountRequest createAccountRequest, AuthenticatedUser user);

    ObjectId addUserAccount(AddUserAccountRequest addUserAccountRequest, AuthenticatedUser user);

    void onNewUserAccount(UserAccount userAccount);
}
