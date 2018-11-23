package net.readybid.api.main.rfp.questionnaire.validators;

/**
 * Created by DejanK on 11/11/2015.
 *
 */
public class InValidator implements QuestionValidator {

    String options[];

    public InValidator(String list) {
        options = list.split(",");
    }

    @Override
    public boolean isNotValid(Object value){
        if(value == null) return false;
        String val = (String) value;
        for (String option : options) {
            if(option.toUpperCase().equals(val.toUpperCase()))
                return false;
        }
        return true;
    }

    @Override
    public String getErrorCode() {
        return "InList";
    }
}
