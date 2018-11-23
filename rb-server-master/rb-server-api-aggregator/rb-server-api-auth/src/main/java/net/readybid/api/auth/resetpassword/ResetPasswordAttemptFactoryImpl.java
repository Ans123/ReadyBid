package net.readybid.api.auth.resetpassword;

import net.readybid.auth.user.UserRegistration;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class ResetPasswordAttemptFactoryImpl implements ResetPasswordAttemptFactory {

    @Override
    public ResetPasswordAttempt createVerifyEmailAddressAttempt(UserRegistration userRegistration, long expiresAt) {
        final ResetPasswordAttemptImpl attempt = new ResetPasswordAttemptImpl();

        attempt.setId(new ObjectId());
        attempt.setUserId(userRegistration.getId());
        attempt.setUserName(userRegistration.getFullName());
        attempt.setEmailAddress(userRegistration.getEmailAddress());
        attempt.setPhoneNumber(userRegistration.getPhone());
        attempt.setState(ResetPasswordAttemptState.VERIFY_EMAIL_ADDRESS);
        attempt.setExpiresAt(expiresAt);

        return attempt;
    }

    @Override
    public ResetPasswordAttempt createVerifyMobilePhoneAttempt(ResetPasswordAttempt attempt, String smsCode, long expiresAt) {
        final ResetPasswordAttemptImpl attemptImpl = new ResetPasswordAttemptImpl(attempt);
        attemptImpl.setSmsCode(smsCode);
        attemptImpl.setExpiresAt(expiresAt);
        attemptImpl.setState(ResetPasswordAttemptState.VERIFY_MOBILE_PHONE);
        return attemptImpl;
    }

    @Override
    public ResetPasswordAttempt createSetPasswordAttempt(ResetPasswordAttempt attempt, long expiresAt) {
        final ResetPasswordAttemptImpl attemptImpl = new ResetPasswordAttemptImpl(attempt);
        attemptImpl.setExpiresAt(expiresAt);
        attemptImpl.setState(ResetPasswordAttemptState.SET_PASSWORD);
        return attemptImpl;
    }
}
