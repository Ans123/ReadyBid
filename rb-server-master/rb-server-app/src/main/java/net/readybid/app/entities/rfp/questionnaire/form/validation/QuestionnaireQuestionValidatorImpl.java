package net.readybid.app.entities.rfp.questionnaire.form.validation;

import net.readybid.app.core.entities.rfp.QuestionnaireQuestion;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;

public class QuestionnaireQuestionValidatorImpl implements QuestionnaireQuestionValidator {

    private final List<QuestionnaireQuestionValidation> validators;
    private QuestionnaireQuestion question;

    public QuestionnaireQuestionValidatorImpl() {
        this(null);
    }

    public QuestionnaireQuestionValidatorImpl(List<QuestionnaireQuestionValidation> validators) {
        this.validators = validators == null ? EMPTY_LIST : Collections.unmodifiableList(validators);
    }

    public void setQuestion(QuestionnaireQuestion questionnaireQuestion) {
        this.question = questionnaireQuestion;
    }

    @Override
    public boolean isValid(Map<String, String> answers, Map<String, String> context) {
        questionCheck();
        final String answer = answers.get(question.getId());
        return (!question.isRequired() || Required.isValid(answer))
                && validators.stream().allMatch(v -> v.isValid(answer, answers, context));
    }

    private void questionCheck() {
        if(question == null) throw new RuntimeException("Question must be set!");
    }

    @Override
    public String getError(Map<String, String> answers, Map<String, String> context) {
        questionCheck();
        final String answer = answers.get(question.getId());
        final List<String> errors = validators.stream()
                .filter( v -> !v.isValid(answer, answers, context))
                .map(v -> v.getClass().getSimpleName())
                .collect(Collectors.toList());
        if(question.isRequired() && !Required.isValid(answer)) errors.add("Mandatory!");
        return errors.isEmpty() ? null : String.format("%s [%s] : ", question.getId(), answer) + errors.toString();
    }

    @Override
    public Map<String, Object> mapValidations() {
        if(validators.isEmpty()) return null;
        List<Map.Entry<String, Object>> validations = validators.stream()
                .map(QuestionnaireQuestionValidation::toMapEntry)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        validations = mergeIfValidations(validations);
        return validations.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public String getRequiredAnswer(String answer, Map<String, String> answers, Map<String, String> context) {
        if(validators.isEmpty()) return null;
        final List<String> requiredAnswers = validators.stream()
                .filter(v -> v instanceof QuestionnaireQuestionDefaultAnswerProvider)
                .map( v -> ((QuestionnaireQuestionDefaultAnswerProvider) v).getDefaultAnswer(answer, answers, context))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return requiredAnswers.size() == 1 ? requiredAnswers.get(0) : null;
    }

    private List<Map.Entry<String, Object>> mergeIfValidations(List<Map.Entry<String, Object>> validations) {
        final List<Map.Entry<String, Object>> mergedValidations = new ArrayList<>();
        List<Object> ifValidations = null;

        for(Map.Entry<String, Object> validation : validations){
            if(validation.getKey().equals("if")){
                if(ifValidations == null){
                    ifValidations = new ArrayList<>();
                    mergedValidations.add(new AbstractMap.SimpleEntry<>("if", ifValidations));
                }
                ifValidations.add(validation.getValue());
            } else {
                mergedValidations.add(validation);
            }
        }

        return mergedValidations;
    }
}
