package net.readybid.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * Created by DejanK on 10/3/2016.
 *
 */
@Size(max = 50)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface Text50 {
    String message() default "Please provide a valid text with max size of 50";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}