package net.readybid.app.core.entities.rfp;

import java.util.List;

public interface QuestionnaireSection {

    String getId();

    String getName();

    String getClasses();

    int getOrd();

    List<QuestionnaireQuestion> getQuestions();

    QuestionnaireSection merge(QuestionnaireSection model, List<QuestionnaireConfigurationItem> sections);

    QuestionnaireSectionType getType();
}
