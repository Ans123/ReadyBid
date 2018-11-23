package net.readybid.app.interactors.rfp_hotel.gateway_dirty;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireConfigurationItem;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.entities.authorization.dirty.InvolvedAccounts;
import net.readybid.app.entities.rfp_hotel.dirty.DeleteBidRequest;
import net.readybid.app.entities.rfp_hotel.dirty.DeleteBidsResult;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 4/3/2017.
 *
 */
public interface BidRepository {

    void updateBidsRfpName(String rfpId, String name, AuthenticatedUser user);

    void updateBidsDueDate(String rfpId, LocalDate dueDate, AuthenticatedUser user);

    void updateBidsProgramPeriod(String rfpId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user);

    void updateBidsCoverLetterTemplate(String rfpId, String sanitizedTemplate);

    void updateBidsQuestionnaireModel(String rfpId, Map<String, Object> questionnaireModel, List<QuestionnaireConfigurationItem> config);

    HotelRfpBid findDeletedBid(ObjectId id, String destinationId, String hotelId);

    void create(HotelRfpBid bid);

    void replace(HotelRfpBid bid);

    HotelRfpBid markCreatedBidAsDeleted(String rfpId, String bidId, HotelRfpBidState state);

    HotelRfpBid getBidById(String bidId);

    void updateBidDueDate(String bidId, LocalDate dueDate, AuthenticatedUser user);

    void updateBidProgramPeriod(String bidId, LocalDate programStartDate, LocalDate programEndDate, AuthenticatedUser user);

    HotelRfpBid getBidWithCoverLetterTemplate(String bidId, AuthenticatedUser user);

    void updateBidCoverLetterTemplate(String bidId, String sanitizedTemplate, AuthenticatedUser user);

    void updateBidQuestionnaireModel(String bidId, Map<String, Object> questionnaireModel, List<QuestionnaireConfigurationItem> questionnaireConfig, AuthenticatedUser user);

    Questionnaire getQuestionnaireModel(String bidId, AuthenticatedUser user);

    InvolvedAccounts getInvolvedAccounts(String id);

    void setSupplierContact(HotelRfpBid bid);

    RfpContact getBidSupplierContact(String bidId);

    HotelRfpSupplier getBidSupplier(String bidId);

    HotelRfpBid getBidById(ObjectId bidId);

    QuestionnaireResponse getResponse(String bidId);

    HotelRfpBid getBidWithRateLoadingInformation(String bidId);

    DeleteBidsResult markAsDeleted(DeleteBidRequest request, HotelRfpBidState state);

    void updateTravelDestination(TravelDestination destination);

    void updateDistancesForDestination(String destinationId);

    HotelRfpBid getBidForResponseDraft(String bidId);

    List<InvolvedAccounts> getInvolvedAccounts(List<String> ids);
}
