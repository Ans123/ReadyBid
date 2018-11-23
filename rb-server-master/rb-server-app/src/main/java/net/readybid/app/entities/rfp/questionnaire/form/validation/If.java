package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;

public class If implements QuestionnaireQuestionValidation, QuestionnaireQuestionDefaultAnswerProvider {

    private final String field;
    private final Condition condition;
    private final List<QuestionnaireQuestionValidation> thenValidations;
    private final List<QuestionnaireQuestionValidation> elseValidations;

    public If(String field, Condition condition, List<QuestionnaireQuestionValidation> thenValidations, List<QuestionnaireQuestionValidation> elseValidations) {
        this.field = field;
        this.condition = condition;
        this.thenValidations = thenValidations == null ? EMPTY_LIST : Collections.unmodifiableList(thenValidations);
        this.elseValidations = elseValidations == null ? EMPTY_LIST : Collections.unmodifiableList(elseValidations);
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        if(conditionIsTrue(answers, context)){
            return isValid(thenValidations, answer, answers, context);
        } else {
            return elseValidations == null || isValid(elseValidations, answer, answers, context);
        }
    }

    private boolean conditionIsTrue(Map<String, String> answers, Map<String, String> context) {
        final String value = ValidationHelper.getValueForField(field, answers, context);
        return condition.isTrue(value);
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        final List<Object> rules = new ArrayList<>();
        rules.add(field);
        rules.add(condition.toList());
        rules.add(map(thenValidations));
        if(!elseValidations.isEmpty()) rules.add(map(elseValidations));

        return new AbstractMap.SimpleEntry<>("if", rules );
    }

    private Map<String, Object> map(List<QuestionnaireQuestionValidation> validations) {
        // there is no IF validations inside IF validation, so IF merging is skipped
        return validations.stream()
                .map(QuestionnaireQuestionValidation::toMapEntry)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isValid(List<QuestionnaireQuestionValidation> validations, String answer, Map<String, String> answers, Map<String, String> context){
        return validations.isEmpty() || validations.stream().allMatch(v -> v.isValid(answer, answers, context));
    }

    @Override
    public String getDefaultAnswer(String answer, Map<String, String> answers, Map<String, String> context) {
        final List<QuestionnaireQuestionValidation> validations = conditionIsTrue(answers, context) ? thenValidations : elseValidations;
        if(validations == null || validations.isEmpty()){
            return null;
        } else {
            final List<String> requiredAnswers = validations.stream()
                    .filter(v -> v instanceof QuestionnaireQuestionDefaultAnswerProvider)
                    .map( v -> ((QuestionnaireQuestionDefaultAnswerProvider) v).getDefaultAnswer(answer, answers, context))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return requiredAnswers.size() == 1 ? requiredAnswers.get(0) : null;
        }
    }

    public interface Condition {
        boolean isTrue(String value);

        List<Object> toList();
    }

    public static class Blank implements Condition {

        private final boolean isBlank;

        public Blank(boolean isBlank) {
            this.isBlank = isBlank;
        }

        @Override
        public boolean isTrue(String value) {
            return (value == null || value.trim().isEmpty()) == isBlank;
        }

        @Override
        public List<Object> toList() {
            return Arrays.asList("isBlank", isBlank);
        }
    }

    public static class Equal implements Condition {

        private final String value;
        private final boolean isEqual;

        public Equal(String value, boolean isEqual) {
            this.value = value;
            this.isEqual = isEqual;
        }

        @Override
        public boolean isTrue(String value) {
            return ( value == null ? this.value == null : value.equals(this.value) ) == isEqual;
        }

        @Override
        public List<Object> toList() {
            return Arrays.asList(isEqual ? "eq" : "ne", value);
        }
    }

    public static class GreaterThen implements Condition {

        private BigDecimal numValue;

        public GreaterThen(String string) {
            numValue = new BigDecimal(string);
        }

        @Override
        public boolean isTrue(String value) {
            try{
                return value != null && new BigDecimal(value).compareTo(numValue) > 0;
            } catch (NumberFormatException ignored){
                return false;
            }
        }

        @Override
        public List<Object> toList() {
            return Arrays.asList("gt", numValue.toString());
        }
    }

    public static class In implements Condition {
        private final List<String> list;

        public In(List<String> list) {
            this.list = list;
        }

        @Override
        public boolean isTrue(String value) {
            return list.contains(value);
        }


        @Override
        public List<Object> toList() {
            return Arrays.asList("in", list);
        }
    }
}
