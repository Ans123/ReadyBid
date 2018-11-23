package net.readybid.auth.login;

import net.readybid.exceptions.BadRequestException;
import net.readybid.validators.captcha.CaptchaValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by DejanK on 3/22/2017.
 *
 */
@Service
public class BruteForceProtectionServiceImpl implements BruteForceProtectionService {

    private static final long ATTEMPTS_MAX_AGE = 900000L; // 15 minutes
    private static final long MAX_GLOBAL_ATTEMPTS_ALLOWED = 1500;
    private static final long MAX_EMAIL_OR_PASSWORD_ATTEMPTS_ALLOWED = 10;

    private final LoginAttemptsRepository loginAttemptsRepository;
    private final CaptchaValidationService captchaValidationService;

    @Autowired
    public BruteForceProtectionServiceImpl(
            LoginAttemptsRepository loginAttemptsRepository,
            CaptchaValidationService captchaValidationService
    ) {
        this.loginAttemptsRepository = loginAttemptsRepository;
        this.captchaValidationService = captchaValidationService;
    }

    @Override
    public void protect(String emailAddress, String password, String captchaToken) {
        final String hashedPassword = hash(password);
        final String hashedEmailAddress = hash(emailAddress.toLowerCase());

        if(shouldProtect(hashedEmailAddress, hashedPassword) && !captchaValidationService.isValid(captchaToken)){
            throw new BadRequestException("INVALID_CAPTCHA_TOKEN");
        } else {
            saveLoginAttempt(hashedEmailAddress, hashedPassword);
        }
    }

    private void saveLoginAttempt(String hashedEmailAddress, String hashedPassword) {
        final long at = new Date().getTime();
        final LoginAttempt passwordAttempt = new LoginAttempt(hashedPassword, at);
        final LoginAttempt emailAttempt = new LoginAttempt(hashedEmailAddress, at);

        loginAttemptsRepository.create(passwordAttempt, emailAttempt);
    }

    private boolean shouldProtect(String hashedEmailAddress, String hashedPassword) {
        loginAttemptsRepository.purgeOldAttempts(ATTEMPTS_MAX_AGE);
        return shouldProtectGlobally() || shouldProtectEmailOrPassword(hashedEmailAddress, hashedPassword);
    }

    private boolean shouldProtectEmailOrPassword(String hashedEmailAddress, String hashedPassword) {
        final List<LoginAttempt> attempts = loginAttemptsRepository.getAttempts(hashedEmailAddress, hashedPassword);
        long passwordAttemptsCount = 0;
        long emailAttemptsCount = 0;

        for(LoginAttempt a : attempts){
            if(hashedEmailAddress.equals(a.target)) {
                emailAttemptsCount++;
            } else if(hashedPassword.equals(a.target)){
                passwordAttemptsCount++;
            }
        }

        return MAX_EMAIL_OR_PASSWORD_ATTEMPTS_ALLOWED < emailAttemptsCount || MAX_EMAIL_OR_PASSWORD_ATTEMPTS_ALLOWED < passwordAttemptsCount;
    }

    private boolean shouldProtectGlobally() {
        return MAX_GLOBAL_ATTEMPTS_ALLOWED <= loginAttemptsRepository.uniqueAttemptsCount();
    }

    private String hash(String string) {
        return Sha512DigestUtils.shaHex(string);
    }
}
