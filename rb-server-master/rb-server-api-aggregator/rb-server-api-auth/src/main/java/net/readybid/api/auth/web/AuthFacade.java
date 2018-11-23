package net.readybid.api.auth.web;

import net.readybid.auth.invitation.SignUpWithInvitationRequest;
import net.readybid.auth.user.AddAccountWithInvitationRequest;
import net.readybid.auth.user.AuthenticatedUser;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by DejanK on 10/4/2016.
 *
 */
public interface AuthFacade {

    void signUp(SignUpRequest signUpRequest);

    void signUpMock(SignUpRequest signUpRequest);

    ObjectId signUpWithInvitation(HttpServletResponse httpServletResponse, SignUpWithInvitationRequest signUpWithInvitationRequest) ;

    void verifyEmailAddress(VerifyEmailAddressRequest verifyEmailAddressRequest);

    Map<String, Object> resetPassword(ResetPasswordRequest resetPasswordRequest);

    void signIn(SignInRequest signInRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    AuthenticatedUser getTokenUser(String token);

    boolean isUser(String emailAddress);

    ObjectId addAccountWithInvitation(HttpServletResponse httpServletResponse, AddAccountWithInvitationRequest addAccountWithInvitationRequest);
}
