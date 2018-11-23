package net.readybid.app.entities.rfp.questionnaire.form.question;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListQuestionnaireQuestion implements QuestionnaireQuestion {

    final QuestionnaireQuestionImpl question;
    final List<ListQuestionnaireQuestionOption> options;

    ListQuestionnaireQuestion(QuestionnaireQuestionImpl question, List<ListQuestionnaireQuestionOption> options) {
        this.question = question;
        this.options = options == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(options);
    }

    @Override
    public QuestionnaireQuestion merge(QuestionnaireQuestion model) {
        return model == null ? null : new ListQuestionnaireQuestion(question.merge(model), options);
    }

    @Override
    public Boolean isValidAnswer(Map<String, String> answers, Map<String, String> context) {
        return question.isValidAnswer(answers, context);
    }

    @Override
    public String getError(Map<String, String> answers, Map<String, String> context) {
        return question.getError(answers, context);
    }

    @Override
    public QuestionnaireQuestionType getType() {
        return question.getType();
    }

    @Override
    public Boolean isReadOnly() {
        return question.isReadOnly();
    }

    @Override
    public Boolean isLocked() {
        return question.isLocked();
    }

    @Override
    public Map<String, Object> mapValidations() {
        return question.mapValidations();
    }

    @Override
    public String getAnswer(Map<String, String> answers, Map<String, String> responseContext) {
        return question.getAnswer(answers, responseContext);
    }

    @Override
    public void parseAnswer(Map<String, String> answers) {
        question.parseAnswer(answers);
        final String answer = answers.get(getId());
        if(!(answer == null || answer.isEmpty())){
            for(ListQuestionnaireQuestionOption option : options){
                if(option.value.equalsIgnoreCase(answer)){
                    answers.put(getId(), option.value);
                    return;
                }
            }
        }
    }

    @Override
    public String getId() {
        return question.getId();
    }

    @Override
    public String getName() {
        return question.getName();
    }

    @Override
    public String getClasses() {
        return question.getClasses();
    }

    @Override
    public Integer getOrd() {
        return question.getOrd();
    }

    @Override
    public String getPlaceholder() {
        return question.getPlaceholder();
    }

    @Override
    public String getDescription() {
        return question.getDescription();
    }

    @Override
    public Boolean isRequired() {
        return question.isRequired();
    }

    public List<ListQuestionnaireQuestionOption> getOptions() {
        return options;
    }
}
