package net.readybid.api.auth.registration;

import net.readybid.api.auth.web.SignUpRequest;
import net.readybid.api.auth.web.VerifyEmailAddressRequest;
import net.readybid.auth.invitation.Invitation;
import net.readybid.auth.invitation.SignUpWithInvitationRequest;
import net.readybid.auth.user.AddAccountWithInvitationRequest;
import net.readybid.auth.user.UserRegistration;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface UserRegistrationService {
    void signUp(SignUpRequest signUpRequest);

    void verifyEmailAddress(VerifyEmailAddressRequest verifyEmailAddressRequest);

    UserRegistration signUpWithInvitation(SignUpWithInvitationRequest signUpWithInvitationRequest, Invitation invitation);

    UserRegistration addAccountWithInvitation(Invitation invitation, AddAccountWithInvitationRequest addAccountWithInvitationRequest);

    UserRegistration findUserRegistration(String emailAddress);
}
