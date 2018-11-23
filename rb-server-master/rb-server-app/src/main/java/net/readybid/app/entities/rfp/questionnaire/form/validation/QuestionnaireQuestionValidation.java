package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.util.Map;

public interface QuestionnaireQuestionValidation {
    boolean isValid(String answer, Map<String,String> answers, Map<String, String> context);

    Map.Entry<String, Object> toMapEntry();
}
