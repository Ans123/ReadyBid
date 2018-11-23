package net.readybid.api.main.bid;

import net.readybid.api.main.rfp.hotelrfp.finalagreement.HotelRfpFinalAgreementService;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.interactors.core.entity.gate.HotelLoader;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidReceivedService;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.app.interactors.rfp_hotel.bid.gate.HotelRfpBidQueryViewLoader;
import net.readybid.app.interactors.rfp_hotel.gateway_dirty.BidRepository;
import net.readybid.app.interactors.rfp_hotel.letter.HotelRfpCoverLetterService;
import net.readybid.app.persistence.mongodb.repository.HotelRepository;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.useraccount.web.UserAccountService;
import net.readybid.entities.Id;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.questionnaire.UpdateQuestionnaireModelRequest;
import net.readybid.rfp.specifications.RfpSpecifications;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.bid.web.BidsQueryRequest;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Projections.include;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection.AMADEUS_PROPCODE;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection.APOLLO_PROPCODE;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection.INTERNALHOTELCODE;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection.PROPCODE;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection.SABRE_PROPCODE;
import static net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection.WRLDSPAN_PROPCODE;
import static net.readybid.mongodb.RbMongoFilters.byId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by DejanK on 4/3/2017.
 *
 */
@Service
public class BidServiceImpl implements BidService {

    private final UserAccountService userAccountService;
    private final BidRepository bidRepository;
    private final HotelRfpCoverLetterService coverLetterService;
    private final HotelRfpFinalAgreementService finalAgreementService;
    private final HotelRfpBidQueryViewLoader bidQueryViewLoader;
    private final HotelRfpBidStateFactory bidStateFactory;
    private final HotelRfpBidReceivedService bidReceivedService;
    private final HotelRepository hotelRepository;

    @Autowired
    public BidServiceImpl(
            UserAccountService userAccountService,
            BidRepository bidRepository,
            HotelRfpCoverLetterService coverLetterService,
            HotelRfpFinalAgreementService finalAgreementService,
            HotelRfpBidQueryViewLoader bidQueryViewLoader,
            HotelRfpBidStateFactory bidStateFactory,
            HotelRfpBidReceivedService bidReceivedService,
            HotelRepository hotelRepository
    ) {
        this.userAccountService = userAccountService;
        this.bidRepository = bidRepository;
        this.coverLetterService = coverLetterService;
        this.finalAgreementService = finalAgreementService;
        this.bidQueryViewLoader = bidQueryViewLoader;
        this.bidStateFactory = bidStateFactory;
        this.bidReceivedService = bidReceivedService;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void updateBidsRfpName(String rfpId, String name, AuthenticatedUser user) {
        // todo: BID AUDIT "RFP NAME CHANGED"
        bidRepository.updateBidsRfpName(rfpId, name, user);
    }

    @Override
    public void updateBidsDueDate(String rfpId, LocalDate dueDate, AuthenticatedUser user) {
        // todo: BID AUDIT "DUE DATE CHANGED"
        bidRepository.updateBidsDueDate(rfpId, dueDate, user);
    }

    @Override
    public void updateBidsProgramPeriod(String rfpId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user) {
        // todo: BID AUDIT "PROGRAM PERIOD CHANGED"
        bidRepository.updateBidsProgramPeriod(rfpId, programStartDate, programEndDate, user);
    }

    @Override
    public void updateBidsCoverLetterTemplate(String rfpId, String sanitizedTemplate) {
        // todo: BID AUDIT "COVER LETTER CHANGED"
        bidRepository.updateBidsCoverLetterTemplate(rfpId, sanitizedTemplate);
    }

    @Override
    public void updateBidsQuestionnaireModel(String rfpId, UpdateQuestionnaireModelRequest updateQuestionnaireRequest) {
        // todo: BID AUDIT "QUESTIONNAIRE CHANGED"
        bidRepository.updateBidsQuestionnaireModel(rfpId, updateQuestionnaireRequest.getModel(), updateQuestionnaireRequest.getConfig());
    }

    @Override
    public HotelRfpBid deleteBid(String rfpId, String bidId, AuthenticatedUser user) {
        // todo: BID AUDIT "BID DELETED"
        final HotelRfpBidState state = bidStateFactory.createSimpleState(HotelRfpBidStateStatus.DELETED, user);
        return bidRepository.markCreatedBidAsDeleted(rfpId, bidId, state);
    }

    @Override
    public List<HotelRfpBidQueryView> query(BidsQueryRequest bidsQueryRequest, AuthenticatedUser user) {
        userAccountService.updateLastUsedBidManager(user, bidsQueryRequest.viewId);
        List<HotelRfpBidQueryView> hotelRfpBidQueryViewList = bidQueryViewLoader.query(bidsQueryRequest, user);
        List<Id> hotelIdList = new ArrayList<Id>();
    	hotelIdList.add(new Id(hotelRfpBidQueryViewList.get(0).$hotelId));
    	 
    	 hotelRfpBidQueryViewList.get(0).$sabrePropcode = hotelRepository.find(byId(hotelIdList), include(PROPCODE, INTERNALHOTELCODE, SABRE_PROPCODE, AMADEUS_PROPCODE, APOLLO_PROPCODE, WRLDSPAN_PROPCODE))
    			.get(0).getAnswers().get("SABRE_PROPCODE"); 
    	
    	/*hotelRfpBidQueryViewList.get(0).answers = hotelLoader.getPropertyCodes(hotelIdList)
                .stream().collect(Collectors.toMap(h -> Id.valueOf(h.getId()), Hotel::getAnswers));*/
    	
        return hotelRfpBidQueryViewList;
    }

    @Override
    public HotelRfpBid getPublicBidPreview(ObjectId bidId) {
        final HotelRfpBid bid = bidRepository.getBidById(bidId);
        bidReceivedService.markBidAsReceivedByGuest(bid);
        coverLetterService.parseLetters(bid, false);
        return bid;
    }

    @Override
    public HotelRfpBid getBidWithPreviews(String bidId, boolean showPlaceholders) {
        bidReceivedService.markBidAsReceivedByUser(bidId);
        final HotelRfpBid bid = bidRepository.getBidWithRateLoadingInformation(bidId);
        coverLetterService.parseLetters(bid, showPlaceholders);
        finalAgreementService.parseFinalAgreementWithDataOrPlaceholders(bid);
        return bid;
    }

    @Override
    public void updateBidDueDate(String bidId, LocalDate dueDate, AuthenticatedUser user) {
        // todo: BID AUDIT "BID DUE DATE CHANGED"
        bidRepository.updateBidDueDate(bidId, dueDate, user);
    }

    @Override
    public void updateBidProgramPeriod(String bidId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user) {
        // todo: BID AUDIT "BID PROGRAM PERIOD CHANGED"
        bidRepository.updateBidProgramPeriod(bidId, programStartDate, programEndDate, user);
    }

    @Override
    public String getBidCoverLetterTemplate(String bidId, AuthenticatedUser user) {
        final HotelRfpBid bid = bidRepository.getBidWithCoverLetterTemplate(bidId, user);
        return bid == null ? null : bid.getCoverLetter();
    }

    @Override
    public void updateBidCoverLetterTemplate(String bidId, String sanitizedTemplate, AuthenticatedUser user) {
        // todo: BID AUDIT "BID COVER LETTER CHANGED"
        bidRepository.updateBidCoverLetterTemplate(bidId, sanitizedTemplate, user);
    }

    @Override
    public Questionnaire getBidQuestionnaireModel(String bidId, AuthenticatedUser user) {
        return bidRepository.getQuestionnaireModel(bidId, user);
    }

    @Override
    public void updateBidQuestionnaireModel(String bidId, UpdateQuestionnaireModelRequest updateQuestionnaireRequest, AuthenticatedUser user) {
        // todo: BID AUDIT "QUESTIONNAIRE CHANGED"
        bidRepository.updateBidQuestionnaireModel(bidId, updateQuestionnaireRequest.getModel(), updateQuestionnaireRequest.getConfig(), user);
    }

    @Override
    public HotelRfpBid getBid(String bidId) {
        return bidRepository.getBidById(bidId);
    }

    @Override
    public Questionnaire getBidQuestionnaireWithResponseDraft(String bidId) {
        bidReceivedService.markBidAsReceivedByUser(bidId);
        final HotelRfpBid bid = bidRepository.getBidForResponseDraft(bidId);
        final Questionnaire questionnaire = bid.getQuestionnaire();
        if( questionnaire.getResponseDraft() == null ) questionnaire.prepareResponseDraft();
        questionnaire.setGlobals(createQuestionnaireGlobals(bid.getSpecifications()));
        return questionnaire;
    }

    private Map<String, String> createQuestionnaireGlobals(RfpSpecifications specifications) {
        final Map<String, String> questionnaireGlobals = new HashMap<>();
        questionnaireGlobals.put("programStartDate", String.valueOf(specifications.getProgramStartDate()));
        questionnaireGlobals.put("programEndDate", String.valueOf(specifications.getProgramEndDate()));
        questionnaireGlobals.put("today", String.valueOf(LocalDate.now()));

        // todo: lastYearRate
        questionnaireGlobals.put("lastYearRate", String.valueOf(1000000));

        return questionnaireGlobals;
    }

    @Override
    public RfpContact getBidSupplierContact(String bidId) {
        return bidRepository.getBidSupplierContact(bidId);
    }

    @Override
    public HotelRfpSupplier getBidSupplier(String bidId) {
        return bidRepository.getBidSupplier(bidId);
    }

    @Override
    public HotelRfpBid sendFinalAgreement(HotelRfpBid bid, AuthenticatedUser user) {
        // TODO: handles only responded and finalized bids for now (doesn't require answers and offer updates)
        // TODO: finalize if not finalized
        return finalAgreementService.sendFinalAgreement(bid, user);
    }

}
