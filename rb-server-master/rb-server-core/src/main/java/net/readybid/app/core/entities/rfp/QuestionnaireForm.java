package net.readybid.app.core.entities.rfp;

import java.util.List;
import java.util.Map;

public interface QuestionnaireForm {

    QuestionnaireForm merge(Questionnaire questionnaire);

    List<QuestionnaireModule> getModules();

    List<String> getErrors(Map<String, String> answers, Map<String, String> context);

    String getId();

    void addDefaultAnswers(Map<String, String> answers, Map<String, String> responseContext);

    void parseAnswers(Map<String, String> updatedAnswers);

    List<QuestionnaireQuestion> getQuestions();
}
