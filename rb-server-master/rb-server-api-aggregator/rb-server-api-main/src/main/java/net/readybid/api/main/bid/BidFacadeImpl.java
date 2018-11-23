package net.readybid.api.main.bid;

import net.readybid.api.main.access.BidAccessControlService;
import net.readybid.api.main.bid.negotiations.HotelRfpNegotiationsService;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationRequest;
import net.readybid.api.main.rfp.hotelrfp.finalagreement.HotelRfpFinalAgreementService;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.questionnaire.UpdateQuestionnaireModelRequest;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;
import net.readybid.rfphotel.bid.web.BidsQueryRequest;
import net.readybid.rfphotel.letter.UpdateLetterTemplateRequest;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import net.readybid.web.RbViewModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by DejanK on 4/5/2017.
 *
 */
@Service
public class BidFacadeImpl implements BidFacade {

    private final BidService bidService;
    private final BidAccessControlService bidAccessControlService;
    private final HotelRfpNegotiationsService hotelRfpNegotiationsService;
    private final HotelRfpFinalAgreementService hotelRfpFinalAgreementService;

    @Autowired
    public BidFacadeImpl(
            BidService bidService,
            BidAccessControlService bidAccessControlService,
            HotelRfpNegotiationsService hotelRfpNegotiationsService,
            HotelRfpFinalAgreementService hotelRfpFinalAgreementService
    ){
        this.bidService = bidService;
        this.bidAccessControlService = bidAccessControlService;
        this.hotelRfpNegotiationsService = hotelRfpNegotiationsService;
        this.hotelRfpFinalAgreementService = hotelRfpFinalAgreementService;
    }

    @Override
    public List<HotelRfpBidQueryView> queryBids(BidsQueryRequest bidsQueryRequest, AuthenticatedUser user) {
        // access control is performed on repository level
        return bidService.query(bidsQueryRequest, user);
    }

    @Override
    public HotelRfpBid getTokenBidPreview(String token) {
        final ObjectId bidId = bidAccessControlService.previewBidWithToken(token);
        return bidService.getPublicBidPreview(bidId) ;
    }

    @Override
    public HotelRfpBid getBidWithPreviews(String bidId, boolean showPlaceholders) {
        bidAccessControlService.readAsAny(bidId);
        return bidService.getBidWithPreviews(bidId, showPlaceholders) ;
    }

    @Override
    public void updateBidDueDate(String bidId, LocalDate dueDate, AuthenticatedUser user) {
        bidAccessControlService.updateAsBuyer(bidId);
        bidService.updateBidDueDate(bidId, dueDate, user);
    }

    @Override
    public void updateBidProgramPeriod(String bidId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user) {
        bidAccessControlService.updateAsBuyer(bidId);
        bidService.updateBidProgramPeriod(bidId, programStartDate, programEndDate, user);
    }

    @Override
    public String getBidCoverLetterTemplate(String bidId, AuthenticatedUser user) {
        bidAccessControlService.readAsBuyer(bidId);
        return bidService.getBidCoverLetterTemplate(bidId, user);
    }

    @Override
    public void updateBidCoverLetterTemplate(String bidId, String sanitizedTemplate, AuthenticatedUser user) {
        bidAccessControlService.updateAsBuyer(bidId);
        bidService.updateBidCoverLetterTemplate(bidId, sanitizedTemplate, user);
    }

    @Override
    public Questionnaire getBidQuestionnaireModel(String bidId, AuthenticatedUser user) {
        bidAccessControlService.readAsBuyer(bidId);
        return bidService.getBidQuestionnaireModel(bidId, user);
    }

    @Override
    public void updateBidQuestionnaire(String bidId, UpdateQuestionnaireModelRequest updateQuestionnaireRequest, AuthenticatedUser user) {
        bidAccessControlService.updateAsBuyer(bidId);
        bidService.updateBidQuestionnaireModel(bidId, updateQuestionnaireRequest, user);
    }

    @Override
    public HotelRfpBid getBidResponse(String bidId) {
        bidAccessControlService.readAsAny(bidId);
        return bidService.getBid(bidId);
    }

    @Override
    public Questionnaire getBidQuestionnaireWithResponseDraft(String bidId) {
        bidAccessControlService.readAsSupplier(bidId);
        return bidService.getBidQuestionnaireWithResponseDraft(bidId);
    }

    @Override
    public RfpContact getBidSupplierContact(String bidId) {
        bidAccessControlService.readAsBuyer(bidId);
        return bidService.getBidSupplierContact(bidId);
    }

    @Override
    public HotelRfpNegotiations getBidNegotiations(String bidId) {
        bidAccessControlService.readAsAny(bidId);
        return hotelRfpNegotiationsService.getNegotiations(bidId);
    }

    @Override
    public RbViewModel addBidNegotiations(String bidId, NegotiationRequest negotiationRequest, AuthenticatedUser user) {
        bidAccessControlService.updateAsAny(bidId);
        return hotelRfpNegotiationsService.addNegotiation(bidId, negotiationRequest, user);
    }

    @Override
    public RbViewModel updateBidNegotiations(String bidId, String negotiationId, NegotiationRequest negotiationRequest, AuthenticatedUser user) {
        bidAccessControlService.updateAsAny(bidId);
        return hotelRfpNegotiationsService.updateBidNegotiations(bidId, negotiationId, negotiationRequest, user);
    }

    @Override
    public RbViewModel addAndFinalizeNegotiations(String bidId, NegotiationRequest negotiationRequest, AuthenticatedUser user) {
        bidAccessControlService.updateAsBuyer(bidId);
        return hotelRfpNegotiationsService.addAndFinalizeNegotiations(bidId, negotiationRequest, user);
    }

    @Override
    public RbViewModel updateAndFinalizeNegotiations(String bidId, String negotiationId, NegotiationRequest negotiationRequest, AuthenticatedUser user) {
        bidAccessControlService.updateAsBuyer(bidId);
        return hotelRfpNegotiationsService.updateAndFinalizeNegotiations(bidId, negotiationId, negotiationRequest, user);
    }

    @Override
    public HotelRfpSupplier getBidSupplier(String bidId) {
        bidAccessControlService.readAsAny(bidId);
        return bidService.getBidSupplier(bidId);
    }

    @Override
    public String getFinalAgreement(String bidId) {
        bidAccessControlService.readAsAny(bidId);
        return hotelRfpFinalAgreementService.getFromBid(bidId);
    }

    @Override
    public HotelRfpBid sendFinalAgreement(String bidId, AuthenticatedUser user) {
        bidAccessControlService.updateAsBuyer(bidId);
        final HotelRfpBid bid = bidService.getBid(bidId);
        return bidService.sendFinalAgreement(bid, user);
    }

    @Override
    public String getBidFinalAgreementTemplate(String bidId) {
        bidAccessControlService.readAsBuyer(bidId);
        return hotelRfpFinalAgreementService.getTemplateFromBid(bidId);
    }

    @Override
    public void updateBidFinalAgreementTemplate(String bidId, UpdateLetterTemplateRequest templateRequest, AuthenticatedUser user) {
        bidAccessControlService.updateAsBuyer(bidId);
        hotelRfpFinalAgreementService.updateTemplateInBid(bidId, templateRequest.getSanitizedTemplate());
    }
}
