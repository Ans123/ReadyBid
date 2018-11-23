package net.readybid.app.gate.api.rfps;

import net.readybid.mongodb.CreationDetailsViewModel;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.specifications.RfpSpecificationsViewModel;
import net.readybid.web.StatusDetailsViewModel;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 4/3/2017.
 *
 */
public class RfpViewModel implements ViewModel<Rfp> {

    public static ViewModelFactory<Rfp, RfpViewModel> FACTORY = RfpViewModel::new;

    public ObjectId id;
    public RfpSpecificationsViewModel specifications;
    public String coverLetter;
    public String namCoverLetter;
    public QuestionnaireView questionnaire;
    public String finalAgreement;

    public CreationDetailsViewModel created;
    public StatusDetailsViewModel status;

    public RfpViewModel(Rfp rfp) {
        id = rfp.getId();
        specifications = RfpSpecificationsViewModel.FACTORY.createAsPartial(rfp.getSpecifications());
        coverLetter = rfp.getCoverLetter();
        namCoverLetter = rfp.getNamCoverLetter();
        questionnaire = QuestionnaireView.FACTORY.createAsPartial(rfp.getQuestionnaire());
        finalAgreement = rfp.getFinalAgreementTemplate();

        created = CreationDetailsViewModel.FACTORY.createAsPartial(rfp.getCreationDetails());
        status = StatusDetailsViewModel.FACTORY.createAsPartial(rfp.getStatus());
    }
}
