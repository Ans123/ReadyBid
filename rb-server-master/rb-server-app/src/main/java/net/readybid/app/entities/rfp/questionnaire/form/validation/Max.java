package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.util.AbstractMap;
import java.util.Map;

public class Max implements QuestionnaireQuestionValidation{

    private final long value;

    public Max(long value) {
        this.value = value;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        return answer == null || answer.isEmpty() || answer.length() <= value;
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("max", value);
    }
}
