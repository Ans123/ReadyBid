package net.readybid.app.entities.rfp.questionnaire.form;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireModule;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireSection;
import net.readybid.app.entities.IBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class QuestionnaireModuleImpl implements QuestionnaireModule {

    private final String id;
    private final String name;
    private final int ord;
    private final List<QuestionnaireSection> sections;

    private QuestionnaireModuleImpl(String id, String name, int ord, List<QuestionnaireSection> sections) {
        this.id = id;
        this.name = name;
        this.ord = ord;
        this.sections = sections == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(sections);
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
    public int getOrd() {
        return ord;
    }

    @Override
    public List<QuestionnaireSection> getSections() {
        return sections;
    }

    @Override
    public QuestionnaireModule merge(QuestionnaireModule model, List<QuestionnaireConfigurationItem> config) {
        if(model == null || !id.equals(model.getId())) return null;
        return new Builder()
                .setId(id)
                .setName(name)
                .setOrd(ord)
                .setSections(mergeSections(model.getSections(), config))
                .build();
    }

    @Override
    public List<QuestionnaireQuestion> getQuestions() {
        return sections.stream()
                .map(QuestionnaireSection::getQuestions)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<QuestionnaireSection> mergeSections(List<QuestionnaireSection> modelSections, List<QuestionnaireConfigurationItem> config) {
        final List<QuestionnaireSection> mergedSections = new ArrayList<>(modelSections.size());
        final Iterator<QuestionnaireSection> templateIterator = sections.iterator();
        final Iterator<QuestionnaireSection> modelIterator = modelSections.iterator();
        QuestionnaireSection template;
        QuestionnaireSection model = null;

        while (templateIterator.hasNext()){
            template = templateIterator.next();
            if(model == null && modelIterator.hasNext()) model = modelIterator.next();
            final QuestionnaireSection mergeResult = template.merge(model, config);
            if(mergeResult != null) {
                mergedSections.add(mergeResult);
                model = null;
            }
        }

        return mergedSections;
    }

    public static class Builder implements IBuilder<QuestionnaireModuleImpl> {

        private String id;
        private String name;
        private int ord;
        private List<QuestionnaireSection> sections;

        @Override
        public QuestionnaireModuleImpl build() {
            return new QuestionnaireModuleImpl(id, name, ord, sections);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setOrd(int ord) {
            this.ord = ord;
            return this;
        }

        public Builder setSections(List<QuestionnaireSection> sections) {
            this.sections = sections;
            return this;
        }
    }
}
