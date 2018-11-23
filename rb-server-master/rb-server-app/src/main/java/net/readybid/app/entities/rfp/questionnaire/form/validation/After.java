package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

public class After implements QuestionnaireQuestionValidation {

    private final String field;
    private final boolean inclusive;

    public After(String field, boolean inclusive) {
        this.field = field;
        this.inclusive = inclusive;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        final String compareToAsString = ValidationHelper.getValueForField(field, answers, context);
        try {
            final LocalDate compareTo = LocalDate.parse(compareToAsString, DateTimeFormatter.ISO_LOCAL_DATE);
            final LocalDate answerDate = LocalDate.parse(answer, DateTimeFormatter.ISO_LOCAL_DATE);
            return inclusive ? !answerDate.isBefore(compareTo) : answerDate.isAfter(compareTo);
        } catch (DateTimeParseException|NullPointerException ex){
            return true;
        }
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("after", Arrays.asList(field, inclusive));
    }
}
