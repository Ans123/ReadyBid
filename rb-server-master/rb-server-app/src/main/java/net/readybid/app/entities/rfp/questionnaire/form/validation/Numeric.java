package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Map;

public class Numeric implements QuestionnaireQuestionValidation {

    private final boolean isNumeric;

    public Numeric(boolean isNumeric) {
        this.isNumeric = isNumeric;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        if(answer == null || answer.isEmpty() || !isNumeric) return true;
        try{
            new BigDecimal(answer);
            return true;
        } catch (Exception ex){
            return false;
        }
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("numeric", isNumeric);
    }
}
