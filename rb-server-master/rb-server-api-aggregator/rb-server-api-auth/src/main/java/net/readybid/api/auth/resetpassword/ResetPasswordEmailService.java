package net.readybid.api.auth.resetpassword;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface ResetPasswordEmailService {
    void sendUserDoesNotExistEmail(String emailAddress);

    void sendResetPasswordInstructionsEmail(ResetPasswordAttempt resetPasswordAttempt, String resetPasswordToken, int tokenTtlInHours);
}
