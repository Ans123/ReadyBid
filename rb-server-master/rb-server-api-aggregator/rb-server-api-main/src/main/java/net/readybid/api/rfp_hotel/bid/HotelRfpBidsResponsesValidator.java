package net.readybid.api.rfp_hotel.bid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;

public class HotelRfpBidsResponsesValidator implements ConstraintValidator<HotelRfpBidsResponses, List<Map<String, Object>>> {
   public boolean isValid(List<Map<String, Object>> obj, ConstraintValidatorContext context) {
      return obj == null || isValid(obj);
   }

   private boolean isValid(List<Map<String, Object>> responses) {
      return responses.stream().allMatch(HotelRfpBidsResponseValidator::isValid);
   }
}