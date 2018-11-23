package net.readybid.rfp.questionnaire;

import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 2/23/2017.
 *
 */
public class UpdateQuestionnaireModelRequest {

    @NotNull
    public Map<String, Object> model;

    @NotNull
    public List<QuestionnaireConfigurationItemRequest> config;

    public Map<String, Object> getModel() {
        return model;
    }

    public List<QuestionnaireConfigurationItem> getConfig() {
        final List<QuestionnaireConfigurationItem> c = new ArrayList<>();
        config.forEach(r -> {
            final QuestionnaireConfigurationItem i = new QuestionnaireConfigurationItem();
            i.id = r.id;
            i.data = r.data;
            c.add(i);
        });
        return c;
    }
}
