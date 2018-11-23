package net.readybid.app.entities.rfp.questionnaire.form;

import net.readybid.app.core.entities.rfp.*;
import net.readybid.app.entities.IBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class QuestionnaireFormImpl implements QuestionnaireForm {

    private final String id;
    private final List<QuestionnaireModule> modules;

    private QuestionnaireFormImpl(String id, List<QuestionnaireModule> modules) {
        this.id = id;
        this.modules = modules == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(modules);
    }

    @Override
    public QuestionnaireForm merge(Questionnaire questionnaire) {
        return merge(questionnaire.getModel(), questionnaire.getConfig());
    }

    @Override
    public List<QuestionnaireModule> getModules() {
        return modules;
    }

    @Override
    public List<String> getErrors(Map<String, String> answers, Map<String, String> context) {
        return getQuestions().stream()
                .map(q -> q.getError(answers, context))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void parseAnswers(Map<String, String> answers) {
        getQuestions().forEach(q -> q.parseAnswer(answers));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void addDefaultAnswers(Map<String, String> answers, Map<String, String> responseContext) {
        getQuestions().forEach(question -> {
            final String answer = question.getAnswer(answers, responseContext);
            if(!(answer == null || answer.isEmpty())) answers.put(question.getId(), answer);
        });
    }

    @Override
    public List<QuestionnaireQuestion> getQuestions() {
        return modules.stream()
                .map(QuestionnaireModule::getQuestions)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private QuestionnaireForm merge(QuestionnaireForm modelForm, List<QuestionnaireConfigurationItem> config) {
        final List<QuestionnaireModule> modelModules = modelForm.getModules();
        final List<QuestionnaireModule> mergedModules = new ArrayList<>(modelModules.size());
        final Iterator<QuestionnaireModule> templateIterator = modules.iterator();
        final Iterator<QuestionnaireModule> modelIterator = modelModules.iterator();
        QuestionnaireModule template;
        QuestionnaireModule model = null;

        while (templateIterator.hasNext()){
            template = templateIterator.next();
            if(model == null && modelIterator.hasNext()) model = modelIterator.next();
            final QuestionnaireModule mergeResult = template.merge(model, config);
            if(mergeResult != null) {
                mergedModules.add(mergeResult);
                model = null;
            }
        }

        return new QuestionnaireFormImpl(modelForm.getId(), mergedModules);
    }

    public static class Builder implements IBuilder<QuestionnaireFormImpl> {
        private String id;
        private List<QuestionnaireModule> modules;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setModules(List<QuestionnaireModule> modules) {
            this.modules = modules;
            return this;
        }

        public QuestionnaireFormImpl build() {
            return new QuestionnaireFormImpl(id, modules);
        }
    }
}
