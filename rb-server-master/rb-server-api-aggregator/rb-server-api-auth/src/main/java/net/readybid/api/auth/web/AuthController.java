package net.readybid.api.auth.web;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.AuthenticatedUserViewModel;
import net.readybid.auth.user.TokenUserDetailsRequest;
import net.readybid.web.ActionResponse;
import net.readybid.web.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created by DejanK on 3/20/2017.
 *
 */
@RestController
public class AuthController {

    private final AuthFacade authFacade;

    @Autowired
    public AuthController(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse handleSignUpRequest(@Valid @RequestBody SignUpRequest signUpRequest) {
        final ActionResponse actionResult = new ActionResponse();
        authFacade.signUp(signUpRequest);
        return actionResult.finalizeAction();
    }

    @RequestMapping(value = "/sign-up-mock", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse handleSignUpRequestMock(@Valid @RequestBody SignUpRequest signUpRequest) {
        final ActionResponse actionResult = new ActionResponse();
        authFacade.signUpMock(signUpRequest);
        return actionResult.finalizeAction();
    }

    @RequestMapping(value = "/verify-email-address",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse handleVerifyEmailAddressRequest(@Valid @RequestBody VerifyEmailAddressRequest verifyEmailAddressRequest){
        final ActionResponse actionResult = new ActionResponse();
        authFacade.verifyEmailAddress(verifyEmailAddressRequest);
        return actionResult.finalizeAction();
    }

    @RequestMapping(value = "/reset-password",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse handleNewResetPasswordRequest(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        final ActionResponse actionResult = new ActionResponse();
        final Map<String, Object> response = authFacade.resetPassword(resetPasswordRequest);
        return actionResult.finalizeAction(response);
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse handleSignInRequest(
            @Valid @RequestBody SignInRequest signInRequest,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ){
        final ActionResponse actionResult = new ActionResponse();
        authFacade.signIn(signInRequest, httpServletRequest, httpServletResponse);
        return actionResult.finalizeAction();
    }

    @RequestMapping(value = "/current-user", method = RequestMethod.POST)
    public GetResponse<AuthenticatedUser, AuthenticatedUserViewModel> getTokenUser (
            @Valid @RequestBody TokenUserDetailsRequest tokenUserDetailsRequest
    ) {
        final GetResponse<AuthenticatedUser, AuthenticatedUserViewModel> response = new GetResponse<>();
        final AuthenticatedUser user = authFacade.getTokenUser(tokenUserDetailsRequest.token);
        return response.finalizeResult(user, AuthenticatedUserViewModel.FACTORY);
    }
}
