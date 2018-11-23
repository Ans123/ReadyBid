package net.readybid.api.auth.resetpassword;


import net.readybid.auth.user.UserRegistration;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface ResetPasswordAttemptFactory {
    ResetPasswordAttempt createVerifyEmailAddressAttempt(UserRegistration userRegistration, long expiresAt);

    ResetPasswordAttempt createVerifyMobilePhoneAttempt(ResetPasswordAttempt attempt, String smsCode, long expiresAt);

    ResetPasswordAttempt createSetPasswordAttempt(ResetPasswordAttempt attempt, long expiresAt);
}
