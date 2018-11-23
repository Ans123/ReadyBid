package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.util.AbstractMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Regex implements QuestionnaireQuestionValidation {

    private final Pattern pattern;

    public Regex(String pattern) {
        Pattern p;
        try{
            p = Pattern.compile(pattern);
        } catch (Exception e){
            p = null;
        }
        this.pattern = p;
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        if(answer == null || answer.isEmpty() || pattern == null) return true;
        return pattern.matcher(answer).matches();
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("regex", pattern);
    }
}
