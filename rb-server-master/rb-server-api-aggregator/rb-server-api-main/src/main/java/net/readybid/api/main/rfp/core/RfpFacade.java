package net.readybid.api.main.rfp.core;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationFilterRequest;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.questionnaire.UpdateQuestionnaireModelRequest;
import net.readybid.rfphotel.CreateBidRequest;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.letter.UpdateLetterTemplateRequest;

import java.time.LocalDate;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
public interface RfpFacade {
    Rfp createRfpFromTemplate(String templateId, AuthenticatedUser user);

    Rfp getRfpWithPreviews(String rfpId);

    void updateRfpName(String rfpId, String name, AuthenticatedUser user);

    void updateRfpDueDate(String rfpId, LocalDate dueDate, AuthenticatedUser user);

    void updateRfpProgramPeriod(String rfpId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user);

    String getRfpCoverLetterTemplate(String rfpId);

    void updateRfpCoverLetterTemplate(String rfpId, String sanitizedTemplate);

    Questionnaire getQuestionnaireModel(String rfpId);

    void updateRfpQuestionnaire(String rfpId, UpdateQuestionnaireModelRequest updateQuestionnaireRequest);

    HotelRfpBid createBid(String rfpId, CreateBidRequest createBidRequest, AuthenticatedUser user);

    HotelRfpBid deleteBid(String rfpId, String bidId, AuthenticatedUser user);

    String getRfpFinalAgreementTemplate(String rfpId);

    void updateRfpFinalAgreementTemplate(String rfpId, UpdateLetterTemplateRequest templateRequest);

    void updateFilter(String rfpId, UpdateTravelDestinationFilterRequest model);
}
