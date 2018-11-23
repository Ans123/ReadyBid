package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.util.AbstractMap;
import java.util.Map;

public class Required implements QuestionnaireQuestionValidation {

    private final boolean isRequired;

    public Required(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public static boolean isValid(String answer){
        return isValid(true, answer);
    }

    private static boolean isValid(boolean isRequired, String answer){
        return !(isRequired && (answer == null || answer.isEmpty()));
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        return isValid(isRequired, answer);
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("required", isRequired);
    }
}
