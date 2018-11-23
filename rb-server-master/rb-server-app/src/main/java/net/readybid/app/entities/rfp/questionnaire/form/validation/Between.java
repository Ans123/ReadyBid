package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Between implements QuestionnaireQuestionValidation {

    private final BigDecimal min;
    private final BigDecimal max;

    public Between(List<BigDecimal> arguments) {
        if(arguments == null || arguments.isEmpty() || arguments.size() < 2){
            this.min = null;
            this.max = null;
        } else {
            this.min = arguments.get(0);
            this.max = arguments.get(1);
        }
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        try {
            final BigDecimal answerAsDecimal = new BigDecimal(answer);
            return answerAsDecimal.compareTo(min) > -1 && answerAsDecimal.compareTo(max) < 1;
        } catch (Exception e){
            return true;
        }
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("between", Arrays.asList(min.longValue(), max.longValue()));
    }
}
