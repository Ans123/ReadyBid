package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;

import java.util.Map;

public interface QuestionnaireQuestionValidator {
    boolean isValid(Map<String, String> answers, Map<String, String> context);

    String getError(Map<String, String> answers, Map<String, String> context);

    Map<String,Object> mapValidations();

    String getRequiredAnswer(String answer, Map<String, String> answers, Map<String, String> context);

    void setQuestion(QuestionnaireQuestion questionnaireQuestion);
}
