package net.readybid.app.gate.api.rfps;

import net.readybid.rfp.template.RfpTemplate;
import net.readybid.rfp.type.RfpType;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 9/12/2016.
 *
 */
@SuppressWarnings("WeakerAccess")
public class RfpTemplateViewModel implements ViewModel<RfpTemplate> {

    public static final ViewModelFactory<RfpTemplate, RfpTemplateViewModel> FACTORY = RfpTemplateViewModel::new;

    public final String id;
    public final String name;
    public final RfpType type;
    public final String description;
    public final String coverLetter;
    public final QuestionnaireView questionnaire;

    public RfpTemplateViewModel(RfpTemplate rfpTemplate) {
        id = rfpTemplate.getId().toString();
        name = rfpTemplate.getName();
        type = rfpTemplate.getType();
        description = rfpTemplate.getDescription();
        coverLetter = rfpTemplate.getCoverLetter();
        questionnaire = QuestionnaireView.FACTORY.createAsPartial(rfpTemplate.getQuestionnaire());
    }
}
