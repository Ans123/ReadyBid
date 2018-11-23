package net.readybid.api.main.rfp.questionnaire.validators;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class MaxLengthValidator implements QuestionValidator {
    private final int maxLength;

    public MaxLengthValidator(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public boolean isNotValid(Object value){
        return value != null && ((String) value).length() > maxLength;
    }

    @Override
    public String getErrorCode() {
        return "MaxLength";
    }
}
