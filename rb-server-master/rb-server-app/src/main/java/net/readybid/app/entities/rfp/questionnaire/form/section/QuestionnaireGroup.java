package net.readybid.app.entities.rfp.questionnaire.form.section;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.core.entities.rfp.QuestionnaireSectionType;
import net.readybid.app.entities.IBuilder;

import java.util.*;

public class QuestionnaireGroup implements QuestionnaireSection {

    private final QuestionnaireSectionType type;
    private final String id;
    private final String name;
    private final int ord;
    private final String classes;
    private final List<QuestionnaireQuestion> questions;

    private QuestionnaireGroup(Builder builder) {
        this.type = builder.type;
        this.id = builder.id;
        this.name = builder.name;
        this.ord = builder.ord;
        this.questions = builder.questions == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(builder.questions);
        this.classes = builder.classes;
    }

    @Override
    public QuestionnaireSection merge(QuestionnaireSection model, List<QuestionnaireConfigurationItem> configList) {
        if(model == null || !id.equals(model.getId())) return null;
        return new QuestionnaireGroup.Builder()
                .setId(id)
                .setName(name)
                .setOrd(ord)
                .setClasses(classes)
                .setQuestions(mergeQuestions(model.getQuestions()))
                .build();
    }

    @Override
    public QuestionnaireSectionType getType() {
        return type;
    }

    private List<QuestionnaireQuestion> mergeQuestions(List<QuestionnaireQuestion> modelQuestions) {
        final List<QuestionnaireQuestion> mergedQuestions = new ArrayList<>(modelQuestions.size());
        final Iterator<QuestionnaireQuestion> templateIterator = questions.iterator();
        final Iterator<QuestionnaireQuestion> modelIterator = modelQuestions.iterator();
        QuestionnaireQuestion template;
        QuestionnaireQuestion model = null;

        while (templateIterator.hasNext()){
            template = templateIterator.next();
            if(model == null && modelIterator.hasNext()) model = modelIterator.next();
            final QuestionnaireQuestion mergeResult = template.merge(model);
            if(mergeResult != null) {
                mergedQuestions.add(mergeResult);
                model = null;
            }
        }

        return mergedQuestions;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getClasses() {
        return classes;
    }

    @Override
    public int getOrd() {
        return ord;
    }

    @Override
    public List<QuestionnaireQuestion> getQuestions() {
        return questions;
    }

    public static class Builder implements IBuilder<QuestionnaireGroup> {

        private QuestionnaireSectionType type;
        private String id;
        private String name;
        private String classes;
        private int ord;
        private List<QuestionnaireQuestion> questions;

        public Builder setType(QuestionnaireSectionType type) {
            this.type = type;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setClasses(String classes) {
            this.classes = classes;
            return this;
        }

        public Builder setOrd(int ord) {
            this.ord = ord;
            return this;
        }

        public Builder setQuestions(List<QuestionnaireQuestion> questions) {
            this.questions = questions;
            return this;
        }

        @Override
        public QuestionnaireGroup build() {
            return new QuestionnaireGroup(this);
        }
    }
}
