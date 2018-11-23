package net.readybid.api.auth.registration;

import net.readybid.auth.user.UserRegistration;
import net.readybid.email.Email;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface UserRegistrationEmailFactory {
    Email createVerifyEmailAddressEmail(UserRegistration userRegistration, String token, int timeToLiveInHours);

    Email createEmailAddressVerificationInProgressEmail(UserRegistration userRegistration);

    Email createUserAlreadyRegisteredEmail(UserRegistration userRegistration);

    Email createNotifyJoeOfNewSignUpEmail(UserRegistration userRegistration);
}
