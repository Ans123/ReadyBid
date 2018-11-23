package net.readybid.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by stefan on 4/11/2016.
 *
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

        String message() default "Password too weak.";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

}


