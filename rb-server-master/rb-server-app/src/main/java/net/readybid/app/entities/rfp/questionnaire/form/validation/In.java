package net.readybid.app.entities.rfp.questionnaire.form.validation;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Collections.EMPTY_LIST;

public class In implements QuestionnaireQuestionValidation {

    private final List<String> inList;

    public In(List<String> inList) {
        this.inList = inList == null ? EMPTY_LIST : Collections.unmodifiableList(inList);
    }

    @Override
    public boolean isValid(String answer, Map<String, String> answers, Map<String, String> context) {
        return answer == null || answer.isEmpty() || inList.contains(answer);
    }

    @Override
    public Map.Entry<String, Object> toMapEntry() {
        return new AbstractMap.SimpleEntry<>("in", inList);
    }
}
