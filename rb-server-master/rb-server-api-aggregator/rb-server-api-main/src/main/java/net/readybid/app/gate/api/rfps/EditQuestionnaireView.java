package net.readybid.app.gate.api.rfps;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.rfp.type.RfpType;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.List;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class EditQuestionnaireView implements ViewModel<Questionnaire> {

    public static final ViewModelFactory<Questionnaire, EditQuestionnaireView> FACTORY = EditQuestionnaireView::new;

    public final RfpType type;
    public final QuestionnaireFormView model;
    public final List<QuestionnaireConfigurationItem> config;

    public EditQuestionnaireView(Questionnaire questionnaire) {
        this.type = questionnaire.getType();
        this.model = QuestionnaireFormView.FACTORY.createAsPartial(questionnaire.getModel());
        this.config = questionnaire.getConfig();
    }
}
