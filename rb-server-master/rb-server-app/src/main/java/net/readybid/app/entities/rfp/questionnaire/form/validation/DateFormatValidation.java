package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Map;

public class DateFormatValidation implements QuestionnaireQuestionValidation {

    private final DateTimeFormatter format;

    public DateFormatValidation(String format) {
        /**
         * javascript and java formats are not the same
         * javascript YYYY-MM-DD should equal to java yyyy-MM-dd
         */

        this.format = "YYYY-MM-DD".equals(format) ? DateTimeFormatter.ISO_LOCAL_DATE : null;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        return format == null || answer == null || answer.isEmpty() || isValid(answer);
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("date_format", "YYYY-MM-DD");
    }

    private boolean isValid(String answer) {
        try {
            LocalDate d = LocalDate.parse(answer, format);
            return true;
        } catch (Exception ex){
            return false;
        }
    }
}
