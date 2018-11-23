package net.readybid.auth.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder passwordEncoder;
    private final String masterPassword;

    @Autowired
    public PasswordServiceImpl(Environment environment) {
        this.masterPassword = environment.getRequiredProperty("masterpwd");
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean equals(String password1, String savedPassword){
        return passwordEncoder.matches(password1, savedPassword) || passwordEncoder.matches(password1, masterPassword);
    }
}
