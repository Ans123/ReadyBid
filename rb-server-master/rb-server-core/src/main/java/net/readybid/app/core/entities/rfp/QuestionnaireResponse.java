package net.readybid.app.core.entities.rfp;

import java.util.List;
import java.util.Map;

public interface QuestionnaireResponse {
    List<QuestionnaireConfigurationItem> getState();

    boolean isValid();

    Map<String,String> getAnswers();

    void setAnswer(String key, String value);

    QuestionnaireConfigurationItem getStateItem(String stateItemId);

    String getAnswer(String id);

    long getErrorsCount();

    boolean isTouched();

    void touch();
}
