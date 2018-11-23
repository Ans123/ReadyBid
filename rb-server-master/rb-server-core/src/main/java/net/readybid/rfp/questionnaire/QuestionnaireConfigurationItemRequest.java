package net.readybid.rfp.questionnaire;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class QuestionnaireConfigurationItemRequest {

    @NotBlank
    public String id;

    @NotNull
    public Map<String, Object> data;


    public QuestionnaireConfigurationItem getQuestionnaireConfigurationItem() {
        final QuestionnaireConfigurationItem i = new QuestionnaireConfigurationItem();

        i.id = id;
        i.data = data;

        return i;
    }
}
