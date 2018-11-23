package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Map;

public class MaxValue implements QuestionnaireQuestionValidation{

    private final BigDecimal max;

    public MaxValue(BigDecimal max) {
        this.max = max;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        if(answer == null || answer.isEmpty() || max == null) return true;
        try{
            final BigDecimal a = new BigDecimal(answer);
            return a.compareTo(max) < 1;
        } catch (Exception ex){
            return false;
        }
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("max_value", max.longValue());
    }
}
