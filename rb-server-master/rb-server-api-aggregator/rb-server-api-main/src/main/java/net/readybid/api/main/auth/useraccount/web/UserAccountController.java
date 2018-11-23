package net.readybid.api.main.auth.useraccount.web;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.auth.useraccount.core.UserAccountCreationService;
import net.readybid.auth.useraccount.web.AbstractUserAccountController;
import net.readybid.auth.useraccount.web.AddUserAccountRequest;
import net.readybid.auth.useraccount.web.CreateAccountRequest;
import net.readybid.web.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by DejanK on 10/3/2016.
 *
 */
@RestController
@RequestMapping(value = "/user-accounts")
public class UserAccountController extends AbstractUserAccountController {

    @Autowired
    public UserAccountController(
            UserAccountCreationService userAccountCreationService
    ) {
        super(userAccountCreationService);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ActionResponse addAccount(
            @Valid @RequestBody AddUserAccountRequest addUserAccountRequest,
            @CurrentUser AuthenticatedUser authUser
    ) {
        return super.handleAddAccountRequest(addUserAccountRequest, authUser);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ActionResponse createAccount(
            @Valid @RequestBody CreateAccountRequest createAccountRequest,
            @CurrentUser AuthenticatedUser authUser) {
        return super.handleCreateAccountRequest(createAccountRequest, authUser);
    }
}