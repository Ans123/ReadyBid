package net.readybid.auth.login;

/**
 * Created by DejanK on 3/22/2017.
 *
 */
public interface BruteForceProtectionService {
    void protect(String emailAddress, String password, String captchaToken);
}
