package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.util.AbstractMap;
import java.util.Map;

public class Min implements QuestionnaireQuestionValidation{

    private final long value;

    public Min(long value) {
        this.value = value;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        return answer == null || answer.isEmpty() || answer.length() >= value;
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("min", value);
    }
}
