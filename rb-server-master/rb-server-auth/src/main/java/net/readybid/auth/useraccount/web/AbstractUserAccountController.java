package net.readybid.auth.useraccount.web;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.useraccount.core.UserAccountCreationService;
import net.readybid.web.ActionResponse;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/24/2017.
 *
 */
public abstract class AbstractUserAccountController {

    private final UserAccountCreationService userAccountCreationService;

    public AbstractUserAccountController(
            UserAccountCreationService userAccountCreationService
    ){
        this.userAccountCreationService = userAccountCreationService;
    }

    protected ActionResponse handleAddAccountRequest(AddUserAccountRequest addUserAccountRequest, AuthenticatedUser user) {
        final ActionResponse actionResult = new ActionResponse();
        final ObjectId accountId = userAccountCreationService.addUserAccount(addUserAccountRequest, user);
        return actionResult.finalizeAction("accountId", accountId);
    }

    protected ActionResponse handleCreateAccountRequest(CreateAccountRequest createAccountRequest, AuthenticatedUser user) {
        final ActionResponse actionResult = new ActionResponse();
        final ObjectId accountId = userAccountCreationService.createUserAccount(createAccountRequest, user);
        return actionResult.finalizeAction("accountId", accountId);
    }
}