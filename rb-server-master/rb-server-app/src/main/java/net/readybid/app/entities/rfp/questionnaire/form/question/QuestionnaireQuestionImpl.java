package net.readybid.app.entities.rfp.questionnaire.form.question;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;
import net.readybid.app.core.entities.rfp.QuestionnaireQuestionType;
import net.readybid.app.entities.rfp.questionnaire.form.validation.QuestionnaireQuestionValidator;

import java.util.Map;

public class QuestionnaireQuestionImpl implements QuestionnaireQuestion {

    final QuestionnaireQuestionType type;
    final String id;
    final String name;
    final String classes;
    final Integer ord;
    final String placeholder;
    final String description;
    final Boolean readOnly;
    final Boolean locked;
    final boolean required;
    final QuestionnaireQuestionValidator validator;

    QuestionnaireQuestionImpl(QuestionnaireQuestionBuilder builder) {
        this.type = builder.type;
        this.id = builder.id;
        this.name = builder.name;
        this.classes = builder.classes;
        this.ord = builder.ord;
        this.placeholder = builder.placeholder;
        this.description = builder.description;
        this.readOnly = builder.readOnly;
        this.locked = builder.locked;
        this.required = builder.required;
        this.validator = builder.getValidator(this);
    }

    @Override
    public QuestionnaireQuestionImpl merge(QuestionnaireQuestion model) {
        return model == null ? null : new QuestionnaireQuestionImpl(
                new QuestionnaireQuestionBuilder()
                        .setType(type)
                        .setId(id)
                        .setName(name)
                        .setClasses(classes)
                        .setOrd(ord)
                        .setPlaceholder(placeholder)
                        .setDescription(description)
                        .setReadOnly(readOnly)
                        .setLocked(locked)
                        .setRequired(required || model.isRequired())
                        .setValidator(validator)
        );
    }

    @Override
    public Boolean isValidAnswer(Map<String, String> answers, Map<String, String> context) {
        return validator.isValid(answers, context);
    }

    @Override
    public String getError(Map<String, String> answers, Map<String, String> context) {
        return validator.getError(answers, context);
    }

    @Override
    public QuestionnaireQuestionType getType() {
        return type;
    }

    @Override
    public Boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public Boolean isLocked() {
        return locked;
    }

    @Override
    public Map<String, Object> mapValidations() {
        return validator.mapValidations();
    }

    @Override
    public String getAnswer(Map<String, String> answers, Map<String, String> context) {
        final String answer = answers.get(id);
        if(answer == null || answer.isEmpty()){
            return validator.getRequiredAnswer(answer, answers, context);
        } else {
            return answer;
        }
    }

    @Override
    public void parseAnswer(Map<String, String> answers) {
        final String answer = answers.get(id);
        if( !(answer == null || answer.isEmpty()) )
            answers.put(id, answer.trim());
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
    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public String getClasses() {
        return classes;
    }

    @Override
    public Integer getOrd() {
        return ord;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Boolean isRequired() {
        return required;
    }
}
