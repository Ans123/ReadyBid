package net.readybid.api.main.rfp.core;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.api.main.bid.BidService;
import net.readybid.api.main.bid.create.CreateHotelRfpBidService;
import net.readybid.app.interactors.rfp_hotel.bid_manager.view.BidManagerViewService;
import net.readybid.api.main.rfp.hotelrfp.finalagreement.HotelRfpFinalAgreementService;
import net.readybid.api.main.rfp.template.RfpTemplateRepository;
import net.readybid.app.core.action.traveldestination.CreateTravelDestinationAction;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.app.core.service.traveldestination.SaveTravelDestinationRepository;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.RfpRepository;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationFilterRequest;
import net.readybid.entities.core.EntityRepository;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.questionnaire.UpdateQuestionnaireModelRequest;
import net.readybid.rfp.template.RfpTemplate;
import net.readybid.rfphotel.CreateBidRequest;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.letter.UpdateLetterTemplateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


/**
 * Created by DejanK on 3/31/2017.
 *
 */
@Service
public class RfpFacadeImpl implements RfpFacade {

    private final RfpService rfpService;
    private final RfpTemplateRepository rfpTemplateRepository;
    private final BidManagerViewService bidManagerViewService;
    private final BidService bidService;
    private final RfpAccessControlService rfpAccessControlService;
    private final EntityRepository entityRepository;
    private final CreateHotelRfpBidService createBidService;
    private final HotelRfpFinalAgreementService finalAgreementService;
    private final CreateTravelDestinationAction travelDestinationFactory;
    private final RfpRepository rfpRepository;
    private final SaveTravelDestinationRepository saveTravelDestinationRepository;
    @Autowired
    public RfpFacadeImpl(
            RfpService rfpService,
            RfpTemplateRepository rfpTemplateRepository,
            BidManagerViewService bidManagerViewService,
            BidService bidService,
            RfpAccessControlService rfpAccessControlService,
            EntityRepository entityRepository,
            CreateHotelRfpBidService createBidService,
            HotelRfpFinalAgreementService finalAgreementService,
            CreateTravelDestinationAction travelDestinationFactory,
            RfpRepository rfpRepository,
            SaveTravelDestinationRepository saveTravelDestinationRepository
    ) {
        this.rfpService = rfpService;
        this.rfpTemplateRepository = rfpTemplateRepository;
        this.bidManagerViewService = bidManagerViewService;
        this.bidService = bidService;
        this.rfpAccessControlService = rfpAccessControlService;
        this.entityRepository = entityRepository;
        this.createBidService = createBidService;
        this.finalAgreementService = finalAgreementService;
        this.travelDestinationFactory = travelDestinationFactory;
        this.rfpRepository = rfpRepository;
        this.saveTravelDestinationRepository = saveTravelDestinationRepository;
    }

    @Override
    public Rfp createRfpFromTemplate(String templateId, AuthenticatedUser user) {
        rfpAccessControlService.create(user.getAccountId());
        final RfpTemplate rfpTemplate = rfpTemplateRepository.getRfpTemplate(templateId);
        final Account account = user.getAccount();
        final Entity entity = entityRepository.findByIdIncludingUnverified(account.getType(), String.valueOf(account.getEntityId()));
        final Rfp rfp = rfpService.createRfp(rfpTemplate, user, entity);
        bidManagerViewService.createRfpView(rfp, user);
        return rfp;
    }

    @Override
    public Rfp getRfpWithPreviews(String rfpId) {
        rfpAccessControlService.read(rfpId);
        return rfpService.getRfpWithPreviews(rfpId);
    }

    @Override
    public void updateRfpName(String rfpId, String name, AuthenticatedUser user) {
        rfpAccessControlService.update(rfpId);
        rfpService.updateRfpName(rfpId, name, user);
        bidManagerViewService.updateRfpViewName(rfpId, name, user);
        bidService.updateBidsRfpName(rfpId, name, user);
    }

    @Override
    public void updateRfpDueDate(String rfpId, LocalDate dueDate, AuthenticatedUser user) {
        rfpAccessControlService.update(rfpId);
        rfpService.updateRfpDueDate(rfpId, dueDate, user);
        bidService.updateBidsDueDate(rfpId, dueDate, user);
    }

    @Override
    public void updateRfpProgramPeriod(String rfpId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user) {
        rfpAccessControlService.update(rfpId);
        rfpService.updateRfpProgramPeriod(rfpId, programStartDate, programEndDate, user);
        bidService.updateBidsProgramPeriod(rfpId, programStartDate, programEndDate, user);
    }

    @Override
    public String getRfpCoverLetterTemplate(String rfpId) {
        rfpAccessControlService.read(rfpId);
        return rfpService.getRfpCoverLetterTemplate(rfpId);
    }

    @Override
    public void updateRfpCoverLetterTemplate(String rfpId, String sanitizedTemplate) {
        rfpAccessControlService.update(rfpId);
        rfpService.updateRfpCoverLetterTemplate(rfpId, sanitizedTemplate);
        bidService.updateBidsCoverLetterTemplate(rfpId, sanitizedTemplate);
    }

    @Override
    public Questionnaire getQuestionnaireModel(String rfpId) {
        rfpAccessControlService.read(rfpId);
        return rfpService.getRfpQuestionnaireModel(rfpId);
    }

    @Override
    public void updateRfpQuestionnaire(String rfpId, UpdateQuestionnaireModelRequest updateQuestionnaireRequest) {
        rfpAccessControlService.update(rfpId);
        rfpService.updateQuestionnaireModel(rfpId, updateQuestionnaireRequest);
        bidService.updateBidsQuestionnaireModel(rfpId, updateQuestionnaireRequest);
    }

    @Override
    public HotelRfpBid createBid(String rfpId, CreateBidRequest request, AuthenticatedUser currentUser) {
        rfpAccessControlService.update(rfpId);
        final Rfp rfp = rfpService.getRfp(rfpId);
        final TravelDestination destination = rfpService.getRfpTravelDestination(rfpId, request.travelDestinationId);

        final HotelRfpBid bid = createBidService.createBid(rfp, destination, request, currentUser);
        rfpService.onBidCreated(rfp, bid, currentUser);
        return bid;
    }

    @Override
    public HotelRfpBid deleteBid(String rfpId, String bidId, AuthenticatedUser user) {
        rfpAccessControlService.delete(rfpId);
        final HotelRfpBid bid = bidService.deleteBid(rfpId, bidId, user);
        rfpService.bidDeleted(bid, user);
        return bid;
    }

    @Override
    public String getRfpFinalAgreementTemplate(String rfpId) {
        rfpAccessControlService.read(rfpId);
        return finalAgreementService.getTemplateFromRfp(rfpId);
    }

    @Override
    public void updateRfpFinalAgreementTemplate(String rfpId, UpdateLetterTemplateRequest templateRequest) {
        rfpAccessControlService.update(rfpId);
        finalAgreementService.updateTemplateInRfp(rfpId, templateRequest.getSanitizedTemplate());
    }

    @Override
    public void updateFilter(String rfpId, UpdateTravelDestinationFilterRequest model) {
        rfpAccessControlService.update(rfpId);
        final TravelDestinationHotelFilter filter = travelDestinationFactory.createFilter(model);
        rfpRepository.setDefaultFilter(rfpId, filter);
        saveTravelDestinationRepository.updateRfpTravelDestinationFilters(rfpId, filter);
    }
}
