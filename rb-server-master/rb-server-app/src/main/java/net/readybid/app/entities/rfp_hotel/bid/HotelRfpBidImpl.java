package net.readybid.app.entities.rfp_hotel.bid;

import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.app.core.entities.location.distance.Distance;
import net.readybid.app.core.entities.location.distance.DistanceImpl;
import net.readybid.app.core.entities.location.distance.DistanceUnit;
import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.entities.rfp.RateLoadingInformation;
import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.core.Rfp;
import net.readybid.rfp.core.RfpImpl;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfp.specifications.RfpSpecifications;
import net.readybid.rfphotel.bid.HotelRfpBidOffer;
import net.readybid.rfphotel.bid.core.*;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiations;
import net.readybid.rfphotel.supplier.HotelRfpSupplier;
import net.readybid.rfphotel.supplier.RfpHotel;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 1/17/2017.
 *
 */
public class HotelRfpBidImpl implements HotelRfpBid, HotelRfpBidSupplierCompanyEntityAndSubject {
    private ObjectId id;

    private Rfp rfp;
    private HotelRfpType hotelRfpBidType = HotelRfpType.DIRECT;
    private TravelDestination subject;
    private Buyer buyer;
    private HotelRfpSupplier supplier;

    private Questionnaire questionnaire;
    private Map<String, Object> analytics;

    private HotelRfpBidOffer offer;
    private HotelRfpNegotiations negotiations;
    private List<? extends RateLoadingInformation> rateLoadingInformation;
    private LocalDate finalAgreementDate;

    private CreationDetails creationDetails;
    private HotelRfpBidState state;

    @Override
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setRfp(Rfp rfp) {
        this.rfp = rfp;
    }

    public void setSubject(TravelDestination destination) {
        this.subject = destination;
    }

    public void setSupplier(HotelRfpSupplier supplier) {
        this.supplier = supplier;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public void setCreationDetails(CreationDetails creationDetails) {
        this.creationDetails = creationDetails;
    }

    public CreationDetails getCreationDetails() {
        return creationDetails;
    }

    @Override
    public ObjectId getBuyerCompanyAccountId() {
        return buyer == null ? null : buyer.getCompanyAccountId();
    }

    @Override
    public RfpHotel getSupplierCompany() {
        return supplier == null ? null : supplier.getCompany();
    }

    @Override
    public ObjectId getSupplierCompanyEntityId() {
        return supplier == null ? null : supplier.getCompanyEntityId();
    }

    @Override
    public RfpContact getSupplierContact() {
        return supplier == null ? null : supplier.getContact();
    }

    @Override
    public String getBuyerCompanyName() {
        return buyer == null ? null : buyer.getCompanyName();
    }

    @Override
    public String getSupplierCompanyName() {
        return supplier == null ? null : supplier.getCompanyName();
    }

    @Override
    public HotelRfpBidOffer getOffer() {
        return offer;
    }

    @Override
    public double getDistance(DistanceUnit distanceUnit) {
        if(analytics == null) return 0;
        switch (distanceUnit){
            case MI:
                return (double) analytics.getOrDefault("distanceMi", 0);
            case KM:
                return (double) analytics.getOrDefault("distanceKm", 0);
            default:
                return 0;
        }
    }

    @Override
    public RfpSpecifications getSpecifications() {
        return rfp == null ? null : rfp.getSpecifications();
    }

    @Override
    public String getCoverLetter() {
        return rfp == null ? null : rfp.getCoverLetter();
    }

    @Override
    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    @Override
    public QuestionnaireResponse getResponseDraft() {
        return questionnaire == null ? null : questionnaire.getResponseDraft();
    }

    @Override
    public void setCoverLetter(String parsedCoverLetter) {
        if(rfp == null) rfp = new RfpImpl();
        rfp.setCoverLetter(parsedCoverLetter);
    }

    public void updateDistance() {
        if(supplier != null && subject != null){
            if(analytics == null) analytics = new HashMap<>();
            final Distance distance = new DistanceImpl(
                    subject.getLocation().getCoordinates(),
                    supplier.getCompany().getCoordinates(),
                    DistanceUnit.KM);

            analytics.put("distanceKm", distance.getDistance(DistanceUnit.KM));
            analytics.put("distanceMi", distance.getDistance(DistanceUnit.MI));
        }
    }

    public Rfp getRfp() {
        return rfp;
    }

    @Override
    public String getRfpName() {
        return null == rfp ? null : rfp.getName();
    }

    public TravelDestination getSubject() {
        return subject;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public HotelRfpSupplier getSupplier() {
        return supplier;
    }

    public Map<String, Object> getAnalytics() {
        return analytics;
    }

    @Override
    public int getProgramYear() {
        return null == rfp ? 0 : rfp.getProgramYear();
    }

    @Override
    public LocalDate getDueDate() {
        return rfp == null ? null : rfp.getDueDate();
    }

    @Override
    public void setOffer(HotelRfpBidOffer offer) {
        this.offer = offer;
    }

    @Override
    public void addToAnalytics(Map<String, Object> analytics) {
        if(this.analytics == null){
            this.analytics = analytics;
        } else {
            this.analytics.putAll(analytics);
        }
    }

    public void setAnalytics(Map<String, Object> analytics) {
        this.analytics = analytics;
    }

    public void setSupplierContact(RfpContact contact) {
        supplier.setContact(contact);
    }

    @Override
    public void setNegotiations(HotelRfpNegotiations negotiations) {
        this.negotiations = negotiations;
    }

    public HotelRfpNegotiations getNegotiations() {
        return negotiations;
    }

    @Override
    public String getFinalAgreementTemplate() {
        return rfp == null ? null : rfp.getFinalAgreementTemplate();
    }

    @Override
    public void setFinalAgreementTemplate(String finalAgreement) {
        if(rfp == null) {
            rfp = new RfpImpl();
        }
        rfp.setFinalAgreementTemplate(finalAgreement);
    }

    @Override
    public void sendFinalAgreement(HotelRfpBidState state) {
        setState(state);
        setFinalAgreementDate(LocalDate.now());
    }

    @Override
    public LocalDate getProgramStartDate() {
        return rfp == null ? null : rfp.getProgramStartDate();
    }

    @Override
    public LocalDate getProgramEndDate() {
        return rfp == null ? null : rfp.getProgramEndDate();
    }

    public QuestionnaireResponse getResponse() {
        return questionnaire == null ? null : questionnaire.getResponse();
    }

    @Override
    public String getResponseAnswer(String id) {
        return questionnaire == null ? null : questionnaire.getResponseAnswer(id);
    }

    @Override
    public Map<String, String> getResponseAnswers() {
        final QuestionnaireResponse response = getResponse();
        return response == null ? null : response.getAnswers();
    }

    @Override
    public String getSupplierCompanyMasterChainId() {
        final RfpHotel hotel = getSupplierCompany();
        return hotel == null ? null : hotel.getMasterChainId();
    }

    @Override
    public ObjectId getSupplierContactCompanyAccountId() {
        return supplier == null ? null : supplier.getContactCompanyAccountId();
    }

    @Override
    public String getSubjectId() {
        return subject == null ? null : subject.getId();
    }

    @Override
    public long getRateLoadingInformationSize() {
        return rateLoadingInformation == null ? 0 : rateLoadingInformation.size();
    }

    @Override
    public RateLoadingInformation getRateLoadingInformation(int i) {
        return rateLoadingInformation == null || rateLoadingInformation.size() < i ? null : rateLoadingInformation.get(i);
    }

    @Override
    public List<? extends RateLoadingInformation> getRateLoadingInformation() {
        return rateLoadingInformation;
    }

    public void setRateLoadingInformation(List<? extends RateLoadingInformation> rateLoadingInformation) {
        this.rateLoadingInformation = rateLoadingInformation;
    }

    public void setFinalAgreementDate(LocalDate finalAgreementDate) {
        this.finalAgreementDate = finalAgreementDate;
    }

    public LocalDate getFinalAgreementDate() {
        return finalAgreementDate;
    }

    @Override
    public ObjectId getBuyerEntityId() {
        return buyer == null ? null : buyer.getCompanyEntityId();
    }

    @Override
    public ObjectId getRfpId() {
        return null == rfp ? null : rfp.getId();
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public void updateAddressAnalytics() {
        final Map<String, String> addressAnalytics = new HashMap<>();
        final Address address = null != this.supplier ? supplier.getCompanyAddress() : null;

        if(address != null) {
            addressAnalytics.put("country", address.printCountry());
            addressAnalytics.put("stateRegion", address.printStateOrRegion());
            addressAnalytics.put("city", address.printCity());
        }

        addToAnalytics(Collections.singletonMap("address", addressAnalytics));
}

    public Coordinates getTravelDestinationCoordinates() {
        return subject == null ? null : subject.getCoordinates();
    }

    public void setState(HotelRfpBidState state) {
        this.state = state;
    }

    public HotelRfpBidState getState() {
        return state;
    }

    @Override
    public HotelRfpBidStateStatus getStateStatus() {
        return state.getStatus();
    }

    @Override
    public HotelRfpBidStatusDetails getSupplierStatusDetails() {
        return state.getSupplierStatusDetails();
    }

    @Override
    public HotelRfpBidStatusDetails getBuyerStatusDetails() {
        return state.getBuyerStatusDetails();
    }

    public void setType(HotelRfpType type) {
        hotelRfpBidType = type;
    }

    public HotelRfpType getType() {
        return hotelRfpBidType;
    }
}
