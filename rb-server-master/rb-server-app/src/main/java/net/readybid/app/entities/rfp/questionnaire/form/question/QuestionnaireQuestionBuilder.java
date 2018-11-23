package net.readybid.app.entities.rfp.questionnaire.form.question;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;
import net.readybid.app.entities.IBuilder;
import net.readybid.app.entities.rfp.questionnaire.form.validation.QuestionnaireQuestionValidator;
import net.readybid.app.entities.rfp.questionnaire.form.validation.QuestionnaireQuestionValidatorImpl;

import java.util.List;

public class QuestionnaireQuestionBuilder implements IBuilder<QuestionnaireQuestion> {

    QuestionnaireQuestionType type;
    String id;
    String name;
    String classes;
    Integer ord;
    String placeholder;
    String description;
    Boolean readOnly;
    Boolean locked;
    boolean required;
    private QuestionnaireQuestionValidator validator;
    private List<ListQuestionnaireQuestionOption> options;

    @Override
    public QuestionnaireQuestion build() {
        final QuestionnaireQuestionImpl question = new QuestionnaireQuestionImpl(this);
        if(type == null) return question;
        switch (type){
            case TEXT:
                return new TextQuestionnaireQuestion(question);
            case DATE:
                return new DateQuestionnaireQuestion(question);
            case LIST:
                return new ListQuestionnaireQuestion(question, options);
            case DECIMAL:
                return new DecimalQuestionnaireQuestion(question);
            case NUMBER:
                return new NumberQuestionnaireQuestion(question);
            case USER_DEFINED:
                return new UserDefinedQuestionnaireQuestion(question);
        }
        return null;
    }

    public QuestionnaireQuestionBuilder setType(QuestionnaireQuestionType type) {
        this.type = type;
        return this;
    }

    public QuestionnaireQuestionBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public QuestionnaireQuestionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public QuestionnaireQuestionBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public QuestionnaireQuestionBuilder setOrd(Integer ord) {
        this.ord = ord;
        return this;
    }

    public QuestionnaireQuestionBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public QuestionnaireQuestionBuilder setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public QuestionnaireQuestionBuilder setLocked(Boolean locked) {
        this.locked = locked;
        return this;
    }

    public QuestionnaireQuestionBuilder setValidator(QuestionnaireQuestionValidator validator) {
        this.validator = validator;
        return this;
    }

    public QuestionnaireQuestionBuilder setOptions(List<ListQuestionnaireQuestionOption> options) {
        this.options = options;
        return this;
    }

    public QuestionnaireQuestionBuilder setRequired(Boolean isRequired) {
        this.required = isRequired == null ? false : isRequired;
        return this;
    }

    public QuestionnaireQuestionBuilder setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    QuestionnaireQuestionValidator getValidator(QuestionnaireQuestion questionnaireQuestion) {
        final QuestionnaireQuestionValidator v = validator == null ? new QuestionnaireQuestionValidatorImpl() : validator;
        v.setQuestion(questionnaireQuestion);
        return v;
    }
}
