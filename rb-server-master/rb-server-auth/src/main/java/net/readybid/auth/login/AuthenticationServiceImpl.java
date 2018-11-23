package net.readybid.auth.login;

import net.readybid.auth.password.PasswordService;
import net.readybid.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/22/2017.
 *
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final BruteForceProtectionService bruteForceProtectionService;
    private final GetActiveUserPasswordRepository passwordRepository;
    private final PasswordService passwordService;

    @Autowired
    public AuthenticationServiceImpl(
            BruteForceProtectionService bruteForceProtectionService,
            GetActiveUserPasswordRepository passwordRepository,
            PasswordService passwordService
    ) {
        this.bruteForceProtectionService = bruteForceProtectionService;
        this.passwordRepository = passwordRepository;
        this.passwordService = passwordService;
    }

    @Override
    public void authenticate(String emailAddress, String password, String captchaToken) {
        bruteForceProtectionService.protect(emailAddress, password, captchaToken);
        final String savedPassword = passwordRepository.getActiveUserPasswordByEmailAddress(emailAddress.toLowerCase());
        if(savedPassword == null || !passwordService.equals(password, savedPassword)){
            throw new UnauthorizedException();
        }
    }
}
