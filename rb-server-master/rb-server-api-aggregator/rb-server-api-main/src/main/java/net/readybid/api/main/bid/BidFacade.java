package net.readybid.api.main.bid;

import net.readybid.rfphotel.bid.core.negotiations.NegotiationRequest;
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

import java.time.LocalDate;
import java.util.List;

/**
 * Created by DejanK on 4/5/2017.
 *
 */
public interface BidFacade {
    List<HotelRfpBidQueryView> queryBids(BidsQueryRequest bidsQueryRequest, AuthenticatedUser user);

    HotelRfpBid getTokenBidPreview(String token);

    HotelRfpBid getBidWithPreviews(String bidId, boolean showPlaceholders);

    void updateBidDueDate(String bidId, LocalDate dueDate, AuthenticatedUser user);

    void updateBidProgramPeriod(String bidId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user);

    String getBidCoverLetterTemplate(String bidId, AuthenticatedUser user);

    void updateBidCoverLetterTemplate(String bidId, String sanitizedTemplate, AuthenticatedUser user);

    Questionnaire getBidQuestionnaireModel(String bidId, AuthenticatedUser user);

    void updateBidQuestionnaire(String bidId, UpdateQuestionnaireModelRequest updateQuestionnaireRequest, AuthenticatedUser user);

    HotelRfpBid getBidResponse(String bidId);

    Questionnaire getBidQuestionnaireWithResponseDraft(String bidId);

    RfpContact getBidSupplierContact(String bidId);

    HotelRfpNegotiations getBidNegotiations(String bidId);

    RbViewModel addBidNegotiations(String bidId, NegotiationRequest negotiationRequest, AuthenticatedUser user);

    RbViewModel updateBidNegotiations(String bidId, String negotiationId, NegotiationRequest negotiationRequest, AuthenticatedUser user);

    RbViewModel addAndFinalizeNegotiations(String bidId, NegotiationRequest negotiationRequest, AuthenticatedUser user);

    RbViewModel updateAndFinalizeNegotiations(String bidId, String negotiationId, NegotiationRequest negotiationRequest, AuthenticatedUser user);

    HotelRfpSupplier getBidSupplier(String bidId);

    String getFinalAgreement(String bidId);

    HotelRfpBid sendFinalAgreement(String bidId, AuthenticatedUser user);

    String getBidFinalAgreementTemplate(String bidId);

    void updateBidFinalAgreementTemplate(String bidId, UpdateLetterTemplateRequest templateRequest, AuthenticatedUser user);
}
