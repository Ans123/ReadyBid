package net.readybid.validators;

import net.readybid.validators.phone.PhoneNumberValidationServiceImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by DejanK on 8/26/2015.
 *
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidationServiceImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "Phone number is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}