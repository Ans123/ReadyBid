package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Map;

public class Decimal implements QuestionnaireQuestionValidation {

    private final long decimals;

    public Decimal(long decimals) {
        this.decimals = decimals < 0 ? 0 : decimals;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        if(answer == null || answer.isEmpty()) return true;
        try {
            final BigDecimal answerAsBigDecimal = new BigDecimal(answer);
            int scale = answerAsBigDecimal.scale();
            return scale <= decimals;
        } catch (NumberFormatException ex){
            return false;
        }
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("decimal", decimals);
    }
}
