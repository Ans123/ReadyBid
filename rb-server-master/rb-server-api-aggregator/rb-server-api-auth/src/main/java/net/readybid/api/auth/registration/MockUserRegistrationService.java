package net.readybid.api.auth.registration;

import net.readybid.api.auth.web.SignUpRequest;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface MockUserRegistrationService {
    void signUp(SignUpRequest signUpRequest);
}
