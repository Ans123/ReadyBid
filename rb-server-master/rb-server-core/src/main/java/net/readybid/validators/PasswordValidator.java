package net.readybid.validators;

// todo import net.readybid.auth.password.PasswordService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by stefan on 4/11/2016.
 *
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        String PASSWORD_PATTERN ="^((?=\\S*?[A-Z])(?=\\S*?[a-z])(?=\\S*?[0-9]).{8,200})$";
        return value == null || value.matches(PASSWORD_PATTERN);
    }
}
