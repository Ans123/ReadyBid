package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.util.AbstractMap;
import java.util.Map;

public class Equal implements QuestionnaireQuestionValidation, QuestionnaireQuestionDefaultAnswerProvider{

    private final String value;
    private final boolean isEqual;

    public Equal(String value, boolean isEqual) {
        this.value = value;
        this.isEqual = isEqual;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        return (answer == null ? value == null : answer.equals(value)) == isEqual;
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>(isEqual ? "value" : "valueNot", value);
    }

    public String getDefaultAnswer(String answer, Map<String, String> answers, Map<String, String> context){
        return isEqual && !isValid(answer, answers, context) ? value : null;
    }
}
