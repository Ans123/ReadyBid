package net.readybid.app.gate.api.rfps;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.rfp.template.QuestionnaireResponseView;
import net.readybid.rfp.type.RfpType;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.List;
import java.util.Map;

public class QuestionnaireView implements ViewModel<Questionnaire> {

    public static final ViewModelFactory<Questionnaire, QuestionnaireView> FACTORY = QuestionnaireView::new;

    public final RfpType type;
    public final QuestionnaireFormView model;
    public final List<QuestionnaireConfigurationItem> config;
    public final List<String> accepted;
    public final Map<String, String> globals;
    public final QuestionnaireResponseView response;
    public final QuestionnaireResponseView responseDraft;

    public QuestionnaireView(Questionnaire questionnaire) {
        this.type = questionnaire.getType();
        this.model = QuestionnaireFormView.FACTORY.createAsPartial(questionnaire.getModel());
        this.config = questionnaire.getConfig();
        this.accepted = questionnaire.getAcceptedRates();
        this.globals = questionnaire.getGlobals();
        this.response = QuestionnaireResponseView.FACTORY.createAsPartial(questionnaire.getResponse());
        this.responseDraft = QuestionnaireResponseView.FACTORY.createAsPartial(questionnaire.getResponseDraft());
    }
}
