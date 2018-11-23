package net.readybid.app.entities.rfp_hotel.bid;

import net.readybid.app.core.entities.location.distance.DistanceUnit;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.entities.rfp.RateLoadingInformation;
import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfp.specifications.RfpSpecifications;
import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.bid.core.HotelRfpBidStatusDetails;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import net.readybid.rfphotel.supplier.RfpHotel;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public abstract class AbstractHotelRfpBid implements HotelRfpBid {

    @Override
    public ObjectId getId() {
        return null;
    }

    @Override
    public CreationDetails getCreationDetails() {
        return null;
    }

    @Override
    public ObjectId getBuyerCompanyAccountId() {
        return null;
    }

    @Override
    public RfpHotel getSupplierCompany() {
        return null;
    }

    @Override
    public double getDistance(DistanceUnit mi) {
        return 0;
    }

    @Override
    public RfpSpecifications getSpecifications() {
        return null;
    }

    @Override
    public String getCoverLetter() {
        return null;
    }

    @Override
    public Questionnaire getQuestionnaire() {
        return null;
    }

    @Override
    public QuestionnaireResponse getResponseDraft() {
        return null;
    }

    @Override
    public void setCoverLetter(String parsedCoverLetter) {}

    @Override
    public HotelRfpType getType() { return null; }

    @Override
    public Buyer getBuyer() {
        return null;
    }

    @Override
    public TravelDestination getSubject() {
        return null;
    }

    @Override
    public HotelRfpSupplier getSupplier() {
        return null;
    }

    @Override
    public Rfp getRfp() {
        return null;
    }

    @Override
    public String getRfpName() {
        return null;
    }

    @Override
    public ObjectId getSupplierCompanyEntityId() {
        return null;
    }

    @Override
    public RfpContact getSupplierContact() {
        return null;
    }

    @Override
    public String getBuyerCompanyName() {
        return null;
    }

    @Override
    public String getSupplierCompanyName() {
        return null;
    }

    @Override
    public HotelRfpBidOffer getOffer() {
        return null;
    }

    @Override
    public Map<String, Object> getAnalytics() {
        return null;
    }

    @Override
    public int getProgramYear() {
        return 0;
    }

    @Override
    public LocalDate getDueDate() {
        return null;
    }

    @Override
    public void setOffer(HotelRfpBidOffer offer) {

    }

    @Override
    public void addToAnalytics(Map<String, Object> analytics) {

    }

    @Override
    public void setSupplierContact(RfpContact contact) {

    }

    @Override
    public void setNegotiations(HotelRfpNegotiations negotiations) {

    }

    @Override
    public HotelRfpNegotiations getNegotiations() {
        return null;
    }

    @Override
    public String getFinalAgreementTemplate() {
        return null;
    }

    @Override
    public void setFinalAgreementTemplate(String finalAgreement) {

    }

    @Override
    public void sendFinalAgreement(HotelRfpBidState state) {

    }

    @Override
    public LocalDate getFinalAgreementDate() {
        return null;
    }

    @Override
    public ObjectId getBuyerEntityId() {
        return null;
    }

    @Override
    public ObjectId getRfpId() {
        return null;
    }

    @Override
    public LocalDate getProgramStartDate() {
        return null;
    }

    @Override
    public LocalDate getProgramEndDate() {
        return null;
    }

    @Override
    public QuestionnaireResponse getResponse() {
        return null;
    }

    @Override
    public String getResponseAnswer(String id) {
        return null;
    }

    @Override
    public Map<String, String> getResponseAnswers() {
        return null;
    }

    @Override
    public String getSupplierCompanyMasterChainId() {
        return null;
    }

    @Override
    public ObjectId getSupplierContactCompanyAccountId() {
        return null;
    }

    @Override
    public HotelRfpBidState getState() {
        return null;
    }

    @Override
    public HotelRfpBidStateStatus getStateStatus() {
        return null;
    }

    @Override
    public HotelRfpBidStatusDetails getSupplierStatusDetails() {
        return null;
    }

    @Override
    public HotelRfpBidStatusDetails getBuyerStatusDetails() {
        return null;
    }

    @Override
    public long getRateLoadingInformationSize() {
        return 0;
    }

    @Override
    public RateLoadingInformation getRateLoadingInformation(int i) {
        return null;
    }

    @Override
    public List<? extends RateLoadingInformation> getRateLoadingInformation() {
        return null;
    }
}
