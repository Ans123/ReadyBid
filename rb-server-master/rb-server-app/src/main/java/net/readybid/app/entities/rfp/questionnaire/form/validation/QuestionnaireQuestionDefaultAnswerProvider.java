package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.util.Map;

public interface QuestionnaireQuestionDefaultAnswerProvider {

    String getDefaultAnswer(String answer, Map<String, String> answers, Map<String, String> context);
}
