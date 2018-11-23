package net.readybid.api.main.bid;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.questionnaire.UpdateQuestionnaireModelRequest;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.web.BidsQueryRequest;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by DejanK on 4/3/2017.
 *
 */
public interface BidService {
    void updateBidsRfpName(String rfpId, String name, AuthenticatedUser user);

    void updateBidsDueDate(String rfpId, LocalDate dueDate, AuthenticatedUser user);

    void updateBidsProgramPeriod(String rfpId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user);

    void updateBidsCoverLetterTemplate(String rfpId, String sanitizedTemplate);

    void updateBidsQuestionnaireModel(String rfpId, UpdateQuestionnaireModelRequest updateQuestionnaireRequest);

    HotelRfpBid deleteBid(String rfpId, String bidId, AuthenticatedUser user);

    List<HotelRfpBidQueryView> query(BidsQueryRequest bidsQueryRequest, AuthenticatedUser user);

    HotelRfpBid getPublicBidPreview(ObjectId bidId);

    HotelRfpBid getBidWithPreviews(String bidId, boolean showPlaceholders);

    void updateBidDueDate(String bidId, LocalDate dueDate, AuthenticatedUser user);

    void updateBidProgramPeriod(String bidId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user);

    String getBidCoverLetterTemplate(String bidId, AuthenticatedUser user);

    void updateBidCoverLetterTemplate(String bidId, String sanitizedTemplate, AuthenticatedUser user);

    Questionnaire getBidQuestionnaireModel(String bidId, AuthenticatedUser user);

    void updateBidQuestionnaireModel(String bidId, UpdateQuestionnaireModelRequest updateQuestionnaireRequest, AuthenticatedUser user);

    HotelRfpBid getBid(String bidId);

    Questionnaire getBidQuestionnaireWithResponseDraft(String bidId);

    RfpContact getBidSupplierContact(String bidId);

    HotelRfpSupplier getBidSupplier(String bidId);

    HotelRfpBid sendFinalAgreement(HotelRfpBid bidId, AuthenticatedUser user);
}
