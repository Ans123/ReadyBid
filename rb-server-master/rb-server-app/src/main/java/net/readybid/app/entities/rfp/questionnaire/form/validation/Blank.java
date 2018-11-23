package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.util.AbstractMap;
import java.util.Map;

public class Blank implements QuestionnaireQuestionValidation {

    private final boolean isBlank;

    public Blank(boolean isBlank) {
        this.isBlank = isBlank;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        return !isBlank || answer == null || answer.isEmpty();
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("blank", isBlank);
    }
}
