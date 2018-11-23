package net.readybid.app.interactors.rfp_hotel.gateway_dirty;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.app.entities.authorization.dirty.InvolvedAccounts;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.core.Rfp;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
public interface RfpRepository {

    void createRfp(Rfp rfp);

    Rfp getRfpById(String rfpId);

    void updateRfpName(String rfpId, String name, AuthenticatedUser user);

    void updateRfpDueDate(String rfpId, LocalDate dueDate, AuthenticatedUser user);

    void updateRfpProgramPeriod(String rfpId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user);

    Rfp getRfpWithCoverLetterTemplate(String rfpId);

    void updateCoverLetterTemplate(String rfpId, String sanitizedTemplate);

    Questionnaire getQuestionnaireModel(String rfpId);

    void updateQuestionnaireModel(String rfpId, Map<String, Object> model, List<QuestionnaireConfigurationItem> config);

    InvolvedAccounts getInvolvedCompanies(String id);

    List<InvolvedAccounts> getInvolvedCompanies(List<String> ids);

    Rfp getRfpWithRateLoadingInformation(String rfpId);

    void setDefaultFilter(String rfpId, TravelDestinationHotelFilter filter);

    TravelDestinationHotelFilter getRfpDefaultFilter(String rfpId);
}
