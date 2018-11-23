package net.readybid.auth.login;

/**
 * Created by DejanK on 3/22/2017.
 *
 */
public interface AuthenticationService {
    void authenticate(String emailAddress, String password, String captchaToken);
}
