package net.readybid.rfphotel.bid.core;

import net.readybid.app.core.entities.location.distance.DistanceUnit;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.entities.rfp.RateLoadingInformationList;
import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfp.specifications.RfpSpecifications;
import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import net.readybid.rfphotel.supplier.RfpHotel;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Map;

/**
 * Created by DejanK on 1/17/2017.
 *
 */
public interface HotelRfpBid extends RateLoadingInformationList {
    ObjectId getId();

    CreationDetails getCreationDetails();

    ObjectId getBuyerCompanyAccountId();

    RfpHotel getSupplierCompany();

    double getDistance(DistanceUnit mi);

    RfpSpecifications getSpecifications();

    String getCoverLetter();

    Questionnaire getQuestionnaire();

    QuestionnaireResponse getResponseDraft();

    void setCoverLetter(String parsedCoverLetter);

    Buyer getBuyer();

    TravelDestination getSubject();

    HotelRfpSupplier getSupplier();

    Rfp getRfp();

    String getRfpName();

    ObjectId getSupplierCompanyEntityId();

    RfpContact getSupplierContact();

    String getBuyerCompanyName();

    String getSupplierCompanyName();

    HotelRfpBidOffer getOffer();

    Map<String, Object> getAnalytics();

    int getProgramYear();

    LocalDate getDueDate();

    void setOffer(HotelRfpBidOffer offer);

    void addToAnalytics(Map<String, Object> analytics);

    void setSupplierContact(RfpContact contact);

    void setNegotiations(HotelRfpNegotiations negotiations);

    HotelRfpNegotiations getNegotiations();

    String getFinalAgreementTemplate();

    void setFinalAgreementTemplate(String finalAgreement);

    void sendFinalAgreement(HotelRfpBidState state);

    LocalDate getFinalAgreementDate();

    ObjectId getBuyerEntityId();

    ObjectId getRfpId();

    LocalDate getProgramStartDate();

    LocalDate getProgramEndDate();

    QuestionnaireResponse getResponse();

    String getResponseAnswer(String id);

    Map<String,String> getResponseAnswers();

    String getSupplierCompanyMasterChainId();

    ObjectId getSupplierContactCompanyAccountId();

    HotelRfpBidState getState();

    HotelRfpBidStateStatus getStateStatus();

    HotelRfpBidStatusDetails getSupplierStatusDetails();

    HotelRfpBidStatusDetails getBuyerStatusDetails();

    HotelRfpType getType();
}
