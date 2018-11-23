package net.readybid.validators;

import org.bson.types.ObjectId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Created by DejanK on 7/16/2018.
 *
 */
public class IdsValidator implements ConstraintValidator<Ids, List<String>> {

    @Override
    public boolean isValid(List<String> ids, ConstraintValidatorContext constraintValidatorContext) {
        return ids == null || ids.isEmpty() || ids.stream().allMatch(IdsValidator::isValid);
    }

    private static boolean isValid(String id) {
        return id != null && ObjectId.isValid(id);
    }
}
