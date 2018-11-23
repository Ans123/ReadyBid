package net.readybid.api.auth.resetpassword;

import net.readybid.email.WithInternetAddress;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface ResetPasswordAttempt extends WithInternetAddress {
    boolean hasExpired();

    String getPhoneNumber();

    boolean shouldVerifyEmailAddress();

    boolean shouldVerifyMobilePhone();

    int getSmsCodeAttemptsCount();

    boolean shouldSetPassword();

    ObjectId getUserId();

    String getSmsCode();

    ObjectId getId();

    String getUserName();

    String getEmailAddress();

    ResetPasswordAttemptState getState();
}
