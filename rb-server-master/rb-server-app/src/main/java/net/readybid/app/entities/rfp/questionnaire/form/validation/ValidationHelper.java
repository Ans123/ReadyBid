package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.util.Map;

class ValidationHelper {
    private ValidationHelper () {}

    static String getValueForField(String field, Map<String, String> answers, Map<String, String> context) {
        if(field == null) return null;
        if(field.startsWith("#")){
            return answers.getOrDefault(field.substring(1), "");
        } else if (field.startsWith("$")){
            return context.getOrDefault(field.substring(1), "");
        } else {
            return field;
        }
    }
}
