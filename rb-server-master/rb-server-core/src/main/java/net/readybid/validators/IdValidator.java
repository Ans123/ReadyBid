package net.readybid.validators;

import org.bson.types.ObjectId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by DejanK on 2/29/2016.
 *
 */
public class IdValidator implements ConstraintValidator<Id, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || ObjectId.isValid(s);
    }
}
