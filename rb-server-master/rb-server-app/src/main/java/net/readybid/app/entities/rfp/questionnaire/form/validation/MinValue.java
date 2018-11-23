package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Map;

public class MinValue implements QuestionnaireQuestionValidation{

    private final BigDecimal min;

    public MinValue(BigDecimal min) {
        this.min = min;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        if(answer == null || answer.isEmpty() || min == null) return true;
        try{
            final BigDecimal a = new BigDecimal(answer);
            return a.compareTo(min) > -1;
        } catch (Exception ex){
            return false;
        }
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("min_value", min.longValue());
    }
}
