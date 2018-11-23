package net.readybid.api.main.rfp.core;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.questionnaire.UpdateQuestionnaireModelRequest;
import net.readybid.rfp.template.RfpTemplate;
import net.readybid.rfphotel.bid.core.HotelRfpBid;

import java.time.LocalDate;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
public interface RfpService {

    Rfp previewRfpTemplate(RfpTemplate template, AuthenticatedUser user, Entity entity);

    Rfp createRfp(RfpTemplate rfpTemplate, AuthenticatedUser user, Entity entity);

    Rfp getRfpWithPreviews(String rfpId);

    void updateRfpName(String rfpId, String name, AuthenticatedUser user);

    void updateRfpDueDate(String rfpId, LocalDate dueDate, AuthenticatedUser user);

    void updateRfpProgramPeriod(String rfpId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user);

    String getRfpCoverLetterTemplate(String rfpId);

    void updateRfpCoverLetterTemplate(String rfpId, String sanitizedTemplate);

    Questionnaire getRfpQuestionnaireModel(String rfpId);

    void updateQuestionnaireModel(String rfpId, UpdateQuestionnaireModelRequest updateQuestionnaireRequest);

    TravelDestination getRfpTravelDestination(String rfpId, String destinationId);

    Rfp getRfp(String rfpId);

    void onBidCreated(Rfp rfp, HotelRfpBid bid, AuthenticatedUser user);

    void bidDeleted(HotelRfpBid bid, AuthenticatedUser user);
}
