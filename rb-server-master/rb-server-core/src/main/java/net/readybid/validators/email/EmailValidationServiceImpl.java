package net.readybid.validators.email;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by stefan on 10/12/2016.
 *
 */
@Service
public class EmailValidationServiceImpl implements EmailValidationService {
    static final Pattern EMAIL_PATTERN =
            Pattern.compile(".+@.+", Pattern.CASE_INSENSITIVE);
    @Override
    public boolean isValid(String email) {
        if(email == null || email.isEmpty()){
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
}
