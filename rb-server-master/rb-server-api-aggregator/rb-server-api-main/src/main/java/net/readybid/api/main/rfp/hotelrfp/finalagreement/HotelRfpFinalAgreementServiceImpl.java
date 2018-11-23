package net.readybid.api.main.rfp.hotelrfp.finalagreement;

import net.readybid.api.main.bid.BidEmailService;
import net.readybid.api.main.entity.rateloadinginformation.RateLoadingInformationRepository;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.interactors.rfp.ParseLetterService;
import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.auth.invitation.Invitation;
import net.readybid.auth.invitation.InvitationService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.rfp.EntityRateLoadingInformation;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.core.RfpStatus;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Service
public class HotelRfpFinalAgreementServiceImpl implements HotelRfpFinalAgreementService {

    private final HotelRfpFinalAgreementRepository finalAgreementRepository;
    private final ParseLetterService parseLetterService;
    private final HotelRfpLetterPlaceholdersFactory hotelRfpLetterPlaceholdersFactory;
    private final InvitationService invitationService;
    private final BidEmailService bidEmailService;
    private final RateLoadingInformationRepository rateLoadingInformationRepository;
    private final HotelRfpBidStateFactory bidStateFactory;

    @Autowired
    public HotelRfpFinalAgreementServiceImpl(
            HotelRfpFinalAgreementRepository finalAgreementRepository,
            ParseLetterService parseLetterService,
            HotelRfpLetterPlaceholdersFactory hotelRfpLetterPlaceholdersFactory,
            InvitationService invitationService,
            BidEmailService bidEmailService,
            RateLoadingInformationRepository rateLoadingInformationRepository,
            HotelRfpBidStateFactory bidStateFactory
    ) {
        this.finalAgreementRepository = finalAgreementRepository;
        this.parseLetterService = parseLetterService;
        this.hotelRfpLetterPlaceholdersFactory = hotelRfpLetterPlaceholdersFactory;
        this.invitationService = invitationService;
        this.bidEmailService = bidEmailService;
        this.rateLoadingInformationRepository = rateLoadingInformationRepository;
        this.bidStateFactory = bidStateFactory;
    }

    @Override
    public String getFromBid(String bidId) {
        final HotelRfpBid bid = finalAgreementRepository.getFinalAgreementWithModelData(bidId);
        return parseFinalAgreement(bid);
    }

    @Override
    public void parseFinalAgreementWithDataOrPlaceholders(Rfp rfp) {
        final Map<String, String> model = hotelRfpLetterPlaceholdersFactory.getFromRfpWithDataOrPlaceholders(rfp);
        final String finalAgreement = parseLetterService.parse(rfp.getFinalAgreementTemplate(), model);
        rfp.setFinalAgreementTemplate(finalAgreement);
    }

    @Override
    public void parseFinalAgreementWithDataOrPlaceholders(HotelRfpBid bid) {
        final Map<String, String> model = hotelRfpLetterPlaceholdersFactory.getFromBidWithDataOrPlaceholders(bid);
        final String finalAgreement = parseLetterService.parse(bid.getFinalAgreementTemplate(), model);
        bid.setFinalAgreementTemplate(finalAgreement);
    }

    @Override
    public HotelRfpBid sendFinalAgreement(HotelRfpBid bid, AuthenticatedUser user) {
        confirmThatRatesHaveBeenAccepted(bid.getQuestionnaire());
        final EntityRateLoadingInformation rateLoadingInformation = confirmRateLoadingInformation(bid.getBuyerEntityId());

        bid.sendFinalAgreement(bidStateFactory.createSimpleState(HotelRfpBidStateStatus.FINAL_AGREEMENT, user));
        final HotelRfpBid updatedBid = finalAgreementRepository.send(bid, rateLoadingInformation.getInformation());

        if(updatedBid == null) { throw new NotFoundException(); }

        notifyFinalAgreementSent(updatedBid, user);
        parseFinalAgreementWithData(updatedBid);
        return updatedBid;
    }

    private void confirmThatRatesHaveBeenAccepted(Questionnaire questionnaire) {
        if(!questionnaire.containsAcceptedRates())
            throw new UnableToCompleteRequestException("NO_ACCEPTED_RATES");
    }

    @Override
    public String getTemplateFromRfp(String rfpId) {
        return finalAgreementRepository.getTemplateFromRfp(rfpId);
    }

    @Override
    public void updateTemplateInRfp(String rfpId, String sanitizedTemplate) {
        final Rfp rfp = finalAgreementRepository.updateTemplateInRfpIfNotInStates(rfpId, sanitizedTemplate, Arrays.asList(RfpStatus.FINALIZED));
        if(rfp != null){
            finalAgreementRepository.updateTemplateInSyncedRfpBids(rfpId, sanitizedTemplate);
        }
    }

    @Override
    public String getTemplateFromBid(String bidId) {
        return finalAgreementRepository.getTemplateFromBid(bidId);
    }

    @Override
    public void updateTemplateInBid(String bidId, String sanitizedTemplate) {
        finalAgreementRepository.updateTemplateInBidIfNotInStates(bidId, sanitizedTemplate, Arrays.asList(HotelRfpBidStateStatus.FINAL_AGREEMENT));
    }

    private void parseFinalAgreementWithData(HotelRfpBid bid) {
        final Map<String, String> model = hotelRfpLetterPlaceholdersFactory.getFromBidWithData(bid);
        final String finalAgreement = parseLetterService.parse(bid.getFinalAgreementTemplate(), model);
        bid.setFinalAgreementTemplate(finalAgreement);
    }

    private EntityRateLoadingInformation confirmRateLoadingInformation(ObjectId buyerEntityId) {
        final EntityRateLoadingInformation entityRateLoadingInformation = rateLoadingInformationRepository.getByEntityId(buyerEntityId);
        if(!entityRateLoadingInformation.containsInformation()) {
            throw new UnableToCompleteRequestException("NO_RATE_LOADING_INFORMATION");
        }
        return entityRateLoadingInformation;
    }

    private void notifyFinalAgreementSent(HotelRfpBid bid, AuthenticatedUser currentUser) {
        final Date expiryDate = Date.from(bid.getProgramEndDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        final Invitation invitation = invitationService.newInvitation(bid.getSupplierContact(), bid.getId(), expiryDate, currentUser);
        bidEmailService.notifyContactOfFinalAgreementReceived(invitation, bid);
    }

    private String parseFinalAgreement(HotelRfpBid bid) {
        final String finalAgreementTemplate = bid.getFinalAgreementTemplate();

        final Map<String, String> model = hotelRfpLetterPlaceholdersFactory.getFromBid(bid);
        return parseLetterService.parse(finalAgreementTemplate, model);
    }
}
