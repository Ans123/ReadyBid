package net.readybid.api.rfp_hotel.bid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class HotelRfpBidsResponseValidator implements ConstraintValidator<HotelRfpBidsResponse, Map<String, Object>> {
   private static final int QUESTION_MAX_LENGTH = 100;
   private static final int ANSWER_MAX_LENGTH = 10000;

   public boolean isValid(Map<String, Object> obj, ConstraintValidatorContext context) {
      return isValid(obj);
   }

   static boolean isValid(Map<String, Object> obj) {
      return obj != null && obj.entrySet().stream().allMatch(HotelRfpBidsResponseValidator::isAnswerValid);
   }

   private static boolean isAnswerValid(Map.Entry<String, Object> answer) {
      return isKeyValid(answer.getKey()) && isValueValid(answer.getValue());
   }

   private static boolean isKeyValid(String key) {
      return key != null && key.length() <= QUESTION_MAX_LENGTH;
   }

   private static boolean isValueValid(Object value) {
      return String.valueOf(value).length() <= ANSWER_MAX_LENGTH;
   }
}