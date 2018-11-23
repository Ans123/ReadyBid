package net.readybid.api.main.rfp.core;

import net.readybid.api.main.rfp.destinations.TravelDestinationService;
import net.readybid.api.main.rfp.hotelrfp.finalagreement.HotelRfpFinalAgreementService;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.RfpRepository;
import net.readybid.app.interactors.rfp_hotel.letter.HotelRfpCoverLetterService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.questionnaire.UpdateQuestionnaireModelRequest;
import net.readybid.rfp.template.RfpTemplate;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
@Service
public class RfpServiceImpl implements RfpService {

    private final RfpFactory rfpFactory;
    private final RfpRepository rfpRepository;
    private final HotelRfpCoverLetterService coverLetterService;
    private final HotelRfpFinalAgreementService finalAgreementService;
    private final TravelDestinationService travelDestinationService;

    @Autowired
    public RfpServiceImpl(
            RfpFactory rfpFactory,
            RfpRepository rfpRepository,
            HotelRfpCoverLetterService coverLetterService,
            HotelRfpFinalAgreementService finalAgreementService,
            TravelDestinationService travelDestinationService
    ) {
        this.rfpFactory = rfpFactory;
        this.rfpRepository = rfpRepository;
        this.coverLetterService = coverLetterService;
        this.finalAgreementService = finalAgreementService;
        this.travelDestinationService = travelDestinationService;
    }

    @Override
    public Rfp previewRfpTemplate(RfpTemplate template, AuthenticatedUser user, Entity entity) {
        return rfpFactory.createRfp(template, user, entity);
    }

    @Override
    public Rfp createRfp(RfpTemplate rfpTemplate, AuthenticatedUser user, Entity entity) {
        final Rfp rfp = rfpFactory.createRfp(rfpTemplate, user, entity);
        rfpRepository.createRfp(rfp);
        return rfp;
    }

    @Override
    public Rfp getRfpWithPreviews(String rfpId) {
        final Rfp rfp = rfpRepository.getRfpWithRateLoadingInformation(rfpId);
        coverLetterService.parseLetters(rfp);
        finalAgreementService.parseFinalAgreementWithDataOrPlaceholders(rfp);
        return rfp;
    }

    @Override
    public void updateRfpName(String rfpId, String name, AuthenticatedUser user) {
        // todo: RFP AUDIT "NAME CHANGED"
        rfpRepository.updateRfpName(rfpId, name, user);
    }

    @Override
    public void updateRfpDueDate(String rfpId, LocalDate dueDate, AuthenticatedUser user) {
        // todo: RFP AUDIT "DUE DATE CHANGED"
        rfpRepository.updateRfpDueDate(rfpId, dueDate, user);
    }

    @Override
    public void updateRfpProgramPeriod(String rfpId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user) {
        // todo: RFP AUDIT "PROGRAM PERIOD CHANGED"
        rfpRepository.updateRfpProgramPeriod(rfpId, programStartDate, programEndDate, user);
    }

    @Override
    public String getRfpCoverLetterTemplate(String rfpId) {
        final Rfp rfp = rfpRepository.getRfpWithCoverLetterTemplate(rfpId);
        return rfp == null ? null : rfp.getCoverLetter();
    }

    @Override
    public void updateRfpCoverLetterTemplate(String rfpId, String sanitizedTemplate) {
        // todo: RFP AUDIT "COVER LETTER CHANGED"
        rfpRepository.updateCoverLetterTemplate(rfpId, sanitizedTemplate);
    }

    @Override
    public Questionnaire getRfpQuestionnaireModel(String rfpId) {
        return rfpRepository.getQuestionnaireModel(rfpId);
    }

    @Override
    public void updateQuestionnaireModel(String rfpId, UpdateQuestionnaireModelRequest updateQuestionnaireRequest) {
        // todo: RFP AUDIT "QUESTIONNAIRE CHANGED"
        rfpRepository.updateQuestionnaireModel(rfpId, updateQuestionnaireRequest.getModel(), updateQuestionnaireRequest.getConfig());
    }

    @Override
    public TravelDestination getRfpTravelDestination(String rfpId, String destinationId) {
        return travelDestinationService.getRfpTravelDestination(rfpId, destinationId);
    }

    @Override
    public Rfp getRfp(String rfpId) {
        return rfpRepository.getRfpById(rfpId);
    }

    @Override
    public void onBidCreated(Rfp rfp, HotelRfpBid bid, AuthenticatedUser user) {
        // todo: RFP AUDIT "BID ADDED"

    }

    @Override
    public void bidDeleted(HotelRfpBid bid, AuthenticatedUser user) {
        // todo: RFP AUDIT "BID DELETED"
    }
}
