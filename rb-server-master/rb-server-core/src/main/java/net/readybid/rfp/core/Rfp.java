package net.readybid.rfp.core;

import net.readybid.app.core.entities.rfp.Questionnaire;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.entities.rfp.RateLoadingInformationList;
import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.specifications.RfpSpecifications;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.time.LocalDate;

/**
 * Created by DejanK on 9/14/2016.
 *
 */
public interface Rfp extends RateLoadingInformationList {

    String getName();

    ObjectId getId();

    String getCoverLetter();

    void setCoverLetter(String coverLetter);

    RfpSpecifications getSpecifications();

    Questionnaire getQuestionnaire();

    Buyer getBuyer();

    CreationDetails getCreationDetails();

    RfpStatusDetails getStatus();

    ObjectId getBuyerCompanyAccountId();

    LocalDate getBidSentDate();

    int getProgramYear();

    LocalDate getDueDate();

    String getFinalAgreementTemplate();

    void setFinalAgreementTemplate(String finalAgreement);

    LocalDate getProgramStartDate();

    LocalDate getProgramEndDate();

    TravelDestinationHotelFilter getDefaultFilter();

    void setDefaultFilter(TravelDestinationHotelFilter defaultFilter);

    String getNamCoverLetter();

    void setNamCoverLetter(String letter);

    boolean isChainSupported();
}
