package net.readybid.app.core.entities.rfp;

import java.util.List;

public interface QuestionnaireModule {
    String getId();

    String getName();

    int getOrd();

    List<QuestionnaireSection> getSections();

    QuestionnaireModule merge(QuestionnaireModule model, List<QuestionnaireConfigurationItem> config);

    List<QuestionnaireQuestion> getQuestions();
}
