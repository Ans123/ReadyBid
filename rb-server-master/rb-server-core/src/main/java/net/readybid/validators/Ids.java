package net.readybid.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by DejanK on 8/26/2015.
 */
@Documented
@Constraint(validatedBy = IdsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Ids {

    String message() default "id error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}