package net.readybid.app.entities.rfp.questionnaire.form.question;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;

import java.util.Map;

public class TextQuestionnaireQuestion implements QuestionnaireQuestion {

    final QuestionnaireQuestionImpl question;

    TextQuestionnaireQuestion(QuestionnaireQuestionImpl question) {
        this.question = question;
    }

    @Override
    public QuestionnaireQuestion merge(QuestionnaireQuestion model) {
        return model == null ? null : new TextQuestionnaireQuestion(question.merge(model));
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

    @Override
    public String getAnswer(Map<String, String> answers, Map<String, String> responseContext) {
        return question.getAnswer(answers, responseContext);
    }

    @Override
    public void parseAnswer(Map<String, String> answers) {
        question.parseAnswer(answers);
    }
}
