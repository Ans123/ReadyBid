package net.readybid.rfp.core;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.entities.rfp.RateLoadingInformation;
import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.specifications.RfpSpecifications;
import net.readybid.rfp.type.RfpType;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by DejanK on 9/14/2016.
 *
 */
public class RfpImpl implements Rfp, HotelRfpBuyerCompanyAndProgramYear {

    private ObjectId id;
    private RfpSpecifications specifications;

    private String coverLetter;
    private String namCoverLetter;
    private String finalAgreement;
    private Questionnaire questionnaire;
    private TravelDestinationHotelFilter defaultFilter;

    private CreationDetails created;
    private RfpStatusDetails status;
    private List<? extends RateLoadingInformation> rateLoadingInformation;

    @Override
    public String getName() {
        return specifications.getName();
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    public RfpType getType() {
        return specifications.getType();
    }

    public RfpSpecifications getSpecifications() {
        return specifications;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    @Override
    public Buyer getBuyer() {
        return specifications == null ? null : specifications.getBuyer();
    }

    @Override
    public CreationDetails getCreationDetails() {
        return created;
    }

    public TravelDestinationHotelFilter getDefaultFilter() { return defaultFilter; }

    public void setDefaultFilter(TravelDestinationHotelFilter defaultFilter) { this.defaultFilter = defaultFilter; }

    @Override
    public String getNamCoverLetter() {
        return namCoverLetter;
    }

    @Override
    public void setNamCoverLetter(String namCoverLetter) {
        this.namCoverLetter = namCoverLetter;
    }

    @Override
    public boolean isChainSupported() {
        return specifications != null && specifications.isChainSupportEnabled();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setSpecifications(RfpSpecifications specifications) {
        this.specifications = specifications;
    }

    public void setCoverLetter(String letter) {
        this.coverLetter = letter;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public void setCreated(CreationDetails created) {
        this.created = created;
    }

    public CreationDetails getCreated() {
        return created;
    }

    public void setStatus(RfpStatusDetails status) {
        this.status = status;
    }

    public RfpStatusDetails getStatus() {
        return status;
    }

    @Override
    public ObjectId getBuyerCompanyAccountId() {
        return specifications == null ? null : specifications.getBuyerCompanyAccountId();
    }

    @Override
    public LocalDate getBidSentDate() {
        return specifications == null ? null : specifications.getBidSentDate();
    }

    public void setBidSentDate(LocalDate sentDate) {
        specifications.setBidSentDate(sentDate);
    }

    @Override
    public int getProgramYear() {
        return specifications == null ? 0 : specifications.getProgramYear();
    }

    @Override
    public LocalDate getDueDate() {
        return specifications == null ? null : specifications.getDueDate();
    }

    @Override
    public String getFinalAgreementTemplate() {
        return finalAgreement;
    }

    public void setFinalAgreementTemplate(String finalAgreement) {
        this.finalAgreement = finalAgreement;
    }

    @Override
    public LocalDate getProgramStartDate() {
        return specifications == null ? null : specifications.getProgramStartDate();
    }

    @Override
    public LocalDate getProgramEndDate() {
        return specifications == null ? null : specifications.getProgramEndDate();
    }

    @Override
    public long getRateLoadingInformationSize() {
        return rateLoadingInformation == null ? 0 : rateLoadingInformation.size();
    }

    @Override
    public List<? extends RateLoadingInformation> getRateLoadingInformation() {
        return rateLoadingInformation;
    }

    @Override
    public RateLoadingInformation getRateLoadingInformation(int i) {
        return rateLoadingInformation == null || rateLoadingInformation.size() < i ? null : rateLoadingInformation.get(i);
    }

    public void setRateLoadingInformation(List<? extends RateLoadingInformation> rateLoadingInformation) {
        this.rateLoadingInformation = rateLoadingInformation;
    }

    public String getBuyerCompanyName() {
        return getBuyer() == null ? "" : getBuyer().getCompanyName();
    }
}
