package net.readybid.api.rfp_hotel.bid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by DejanK on 7/20/2018.
 */
@Documented
@Constraint(validatedBy = HotelRfpBidsResponseValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface HotelRfpBidsResponse {

    String message() default "Unacceptable response found!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}