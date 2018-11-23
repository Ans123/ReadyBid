package net.readybid.app.core.entities.rfp;

import java.util.Map;

public interface QuestionnaireQuestion {

    String getId();

    Integer getOrd();

    String getName();

    String getPlaceholder();

    String getDescription();

    String getClasses();

    Boolean isRequired();

    QuestionnaireQuestion merge(QuestionnaireQuestion model);

    Boolean isValidAnswer(Map<String, String> answers, Map<String, String> context);

    String getError(Map<String, String> answers, Map<String, String> context);

    QuestionnaireQuestionType getType();

    Boolean isReadOnly();

    Boolean isLocked();

    Map<String,Object> mapValidations();

    String getAnswer(Map<String, String> answers, Map<String, String> responseContext);

    void parseAnswer(Map<String, String> answers);
}
