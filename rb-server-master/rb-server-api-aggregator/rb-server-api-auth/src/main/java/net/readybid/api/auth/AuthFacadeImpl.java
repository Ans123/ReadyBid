package net.readybid.api.auth;

import net.readybid.auth.authorization.AuthorizationService;
import net.readybid.auth.login.AuthenticationService;
import net.readybid.api.auth.registration.MockUserRegistrationService;
import net.readybid.api.auth.registration.UserRegistrationService;
import net.readybid.api.auth.resetpassword.ResetPasswordService;
import net.readybid.api.auth.web.*;
import net.readybid.auth.invitation.Invitation;
import net.readybid.auth.invitation.InvitationService;
import net.readybid.auth.invitation.SignUpWithInvitationRequest;
import net.readybid.auth.user.AddAccountWithInvitationRequest;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.AuthenticatedUserService;
import net.readybid.auth.user.UserRegistration;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by DejanK on 10/4/2016.
 *
 */
@Service
public class AuthFacadeImpl implements AuthFacade {

    private final AuthenticationService authenticationService;
    private final UserRegistrationService userRegistrationService;
    private final MockUserRegistrationService mockUserRegistrationService;
    private final ResetPasswordService resetPasswordService;
    private final InvitationService invitationService;
    private final AuthenticatedUserService authenticatedUserService;
    private final AuthorizationService authorizationService;


    @Autowired
    public AuthFacadeImpl(
            AuthenticationService authenticationService,
            UserRegistrationService userRegistrationService,
            MockUserRegistrationService mockUserRegistrationService,
            ResetPasswordService resetPasswordService,
            InvitationService invitationService,
            AuthenticatedUserService authenticatedUserService,
            AuthorizationService authorizationService
    ) {
        this.authenticationService = authenticationService;
        this.userRegistrationService = userRegistrationService;
        this.mockUserRegistrationService = mockUserRegistrationService;
        this.resetPasswordService = resetPasswordService;
        this.invitationService = invitationService;
        this.authenticatedUserService = authenticatedUserService;
        this.authorizationService = authorizationService;
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        userRegistrationService.signUp(signUpRequest);
    }

    @Override
    public void signUpMock(SignUpRequest signUpRequest) {
        mockUserRegistrationService.signUp(signUpRequest);
    }

    @Override
    public ObjectId signUpWithInvitation(HttpServletResponse httpServletResponse, SignUpWithInvitationRequest signUpWithInvitationRequest) {
        final Invitation invitation = invitationService.getInvitation(signUpWithInvitationRequest.token);
        final UserRegistration userRegistration = userRegistrationService.signUpWithInvitation(signUpWithInvitationRequest, invitation);
        authorizationService.authenticateResponse(httpServletResponse, userRegistration);
        return userRegistration.getCurrentUserAccountId();
}

    @Override
    public void verifyEmailAddress(VerifyEmailAddressRequest verifyEmailAddressRequest) {
        userRegistrationService.verifyEmailAddress(verifyEmailAddressRequest);
    }

    @Override
    public Map<String, Object> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return resetPasswordService.resetPasswordAttempt(resetPasswordRequest);
    }

    @Override
    public void signIn(SignInRequest signInRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        authenticationService.authenticate(signInRequest.email, signInRequest.password, signInRequest.captcha);
        final UserRegistration userRegistration =
                userRegistrationService.findUserRegistration(signInRequest.email.toLowerCase());
        authorizationService.authenticateResponse(httpServletResponse, userRegistration, signInRequest.rememberMe);
    }

    @Override
    public AuthenticatedUser getTokenUser(String token) {
        final Invitation invitation = invitationService.getInvitation(token);
        return authenticatedUserService.getUserByEmailAddressAndAccountId(invitation.getEmailAddress(), invitation.getAccountId());
    }

    @Override
    public boolean isUser(String emailAddress) {
        return authenticatedUserService.isUser(emailAddress);
    }

    @Override
    public ObjectId addAccountWithInvitation(HttpServletResponse httpServletResponse, AddAccountWithInvitationRequest addAccountWithInvitationRequest) {
        final Invitation invitation = invitationService.getInvitation(addAccountWithInvitationRequest.token);
        final UserRegistration userRegistration =
                userRegistrationService.addAccountWithInvitation(invitation, addAccountWithInvitationRequest);
        authorizationService.authenticateResponse(httpServletResponse, userRegistration);
        return userRegistration.getCurrentUserAccountId();
    }
}
