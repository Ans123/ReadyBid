package net.readybid.api.main.rfp.questionnaire.validators;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class MaxValidator implements QuestionValidator {
    private long max;

    public MaxValidator(long max) {
        this.max = max;
    }

    @Override
    public boolean isNotValid(Object value){
        if(value == null) return false;
        if(value instanceof Long) {
            return (Long)value > max;
        } else {
            return (Double)value > max;
        }
    }

    @Override
    public String getErrorCode() {
        return "Max";
    }
}
