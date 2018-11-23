package net.readybid.api.main.auth;

import net.readybid.auth.authorization.AuthorizationService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.AuthenticatedUserService;
import net.readybid.auth.user.AuthenticatedUserViewModel;
import net.readybid.auth.user.CurrentUser;
import net.readybid.auth.useraccount.web.UserAccountService;
import net.readybid.auth.useraccount.web.UserAccountViewModel;
import net.readybid.utils.ListResult;
import net.readybid.web.ActionResponse;
import net.readybid.web.GetResponse;
import net.readybid.web.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by DejanK on 3/20/2017.
 *
 */
@RestController
public class AuthController {

    private final AuthenticatedUserService authenticatedUserService;
    private final UserAccountService userAccountService;
    private final AuthorizationService authorizationService;

    @Autowired
    public AuthController(
            AuthenticatedUserService authenticatedUserService,
            UserAccountService userAccountService,
            AuthorizationService authorizationService
    ) {
        this.authenticatedUserService = authenticatedUserService;
        this.userAccountService = userAccountService;
        this.authorizationService = authorizationService;
    }

    @RequestMapping(value = "/current-user", method = RequestMethod.GET)
    public GetResponse<AuthenticatedUser, AuthenticatedUserViewModel> getCurrentUser(
            @CurrentUser AuthenticatedUser user,
            HttpServletResponse httpServletResponse
    ) {
        final GetResponse<AuthenticatedUser, AuthenticatedUserViewModel> response = new GetResponse<>();
        if(null == user.getCurrentUserAccountId() && user.getUserAccounts() != null && user.getUserAccounts().size() > 0){
            authenticatedUserService.switchToUserAccount(user, new ArrayList<>(user.getUserAccounts()).get(0));
            authorizationService.refreshAuthentication(httpServletResponse, user);
        }
        return response.finalizeResult(user, AuthenticatedUserViewModel.FACTORY);
    }

    @RequestMapping(value = "/current-user/user-accounts")
    public ListResponse<UserAccountViewModel> listCurrentUserUserAccounts(@CurrentUser AuthenticatedUser currentUser){
        final ListResponse<UserAccountViewModel> userAccountsResponse = new ListResponse<>();
        final ListResult<UserAccountViewModel> userAccounts = userAccountService.listUserAccountsForUser(currentUser);
        return userAccountsResponse.finalizeResult(userAccounts);
    }

    @RequestMapping(value = "/current-user/tutorials/{tutorialName}", method = RequestMethod.DELETE)
    public ActionResponse disableTutorial(
            @PathVariable("tutorialName") String tutorialName,
            @CurrentUser AuthenticatedUser currentUser
    ){
        final ActionResponse response = new ActionResponse();
        System.out.println("tutorialName = " + tutorialName);
        authenticatedUserService.disableTutorial(currentUser, tutorialName);
        return response.finalizeAction();
    }

    @RequestMapping(value = "/current-user/account", method = RequestMethod.POST)
    public ActionResponse switchToAccount(
            @CurrentUser AuthenticatedUser currentUser,
            @Valid @RequestBody SwitchToAccountRequest switchToAccountRequest,
            HttpServletResponse httpServletResponse
    ){
        final ActionResponse response = new ActionResponse();
        authenticatedUserService.switchToAccount(currentUser, switchToAccountRequest.accountId);
        authorizationService.refreshAuthentication(httpServletResponse, currentUser);
        return response.finalizeAction();
    }
}
