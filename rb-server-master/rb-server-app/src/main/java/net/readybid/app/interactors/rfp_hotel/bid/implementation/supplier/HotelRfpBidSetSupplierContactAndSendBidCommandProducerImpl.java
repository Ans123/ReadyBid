package net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.bidmanagerview.HotelRfpCreateChainRfpBidManagerViewCommand;
import net.readybid.entities.Id;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;

import java.time.LocalDate;
import java.util.*;

public class HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl implements HotelRfpBidSetSupplierContactAndSendBidCommandProducer {

    private static final Set<HotelRfpBidStateStatus> SAVE_AND_SEND_VALID_STATUSES =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(HotelRfpBidStateStatus.CREATED, HotelRfpBidStateStatus.SENT, HotelRfpBidStateStatus.RECEIVED)));

    private Id bidId;
    private HotelRfpBidStateStatus bidStatus;
    private HotelRfpType currentBidType;
    private boolean rfpChainSupport;
    private Id rfpId;
    private String rfpName;
    private String coverLetterTemplate;
    private String namCoverLetterTemplate;
    private QuestionnaireResponse defaultResponse;
    private HotelRfpSupplierContact currentSupplier;
    private HotelRfpSupplierContact newSupplier;
    private EntityType newSupplierType;
    private Id newSupplierAccountId;

    @Override
    public HotelRfpBidSetSupplierContactCommand generateSetContactCommand(HotelRfpSupplierContact supplierContact) {
        if(!Objects.equals(bidStatus, HotelRfpBidStateStatus.CREATED)) return null;
        setNewSupplier(supplierContact);
        final HotelRfpBidSetSupplierContactCommand.Builder builder = new HotelRfpBidSetSupplierContactCommand.Builder(bidId, bidStatus, newSupplier);
        addBidTypeChange(builder);
        return builder.build();
    }

    @Override
    public HotelRfpBidSetSupplierContactCommand generateSetContactAndSendBidCommand(HotelRfpSupplierContact supplierContact, HotelRfpBidState bidState, LocalDate sentDate) {
        if(!SAVE_AND_SEND_VALID_STATUSES.contains(bidStatus)) return null;
        setNewSupplier(supplierContact);
        final HotelRfpBidSetSupplierContactCommand.Builder builder = new HotelRfpBidSetSupplierContactCommand.Builder(bidId, bidStatus, newSupplier);
        builder.setSendBidData(bidState, sentDate);
        addBidTypeChange(builder);
        addDraftResponseReset(builder);
        return builder.build();
    }

    @Override
    public HotelRfpBidSetSupplierContactCommand generateSendBidCommand(HotelRfpBidState bidState, LocalDate sentDate) {
        if(!SAVE_AND_SEND_VALID_STATUSES.contains(bidStatus)) return null;
        setNewSupplier(currentSupplier);
        final HotelRfpBidSetSupplierContactCommand.Builder builder = new HotelRfpBidSetSupplierContactCommand.Builder(bidId, bidStatus);
        builder.setSendBidData(bidState, sentDate);
        return builder.build();
    }

    @Override
    public HotelRfpCreateChainRfpBidManagerViewCommand generateCreateChainRfpView() {
        return isSupplierChainRepresentative()
                ? new HotelRfpCreateChainRfpBidManagerViewCommand(rfpName, rfpId, newSupplierAccountId) : null;
    }

    private boolean isSupplierChainRepresentative() {
        return Objects.equals(newSupplierType, EntityType.CHAIN);
    }

    private void addBidTypeChange(HotelRfpBidSetSupplierContactCommand.Builder builder) {
        if(rfpChainSupport && isContactNotMatchingBidType())
            builder.changeBidType(getBidTypeForSupplierType(), getCoverLetterForSupplierType());
    }

    private String getCoverLetterForSupplierType() {
        return isSupplierChainRepresentative() ? namCoverLetterTemplate : coverLetterTemplate;
    }

    private HotelRfpType getBidTypeForSupplierType() {
        return isSupplierChainRepresentative() ? HotelRfpType.CHAIN : HotelRfpType.DIRECT;
    }

    private boolean isContactNotMatchingBidType() {
        return !((isSupplierChainRepresentative() && Objects.equals(currentBidType, HotelRfpType.CHAIN))
                || (!isSupplierChainRepresentative() && Objects.equals(currentBidType, HotelRfpType.DIRECT)));
    }

    private void addDraftResponseReset(HotelRfpBidSetSupplierContactCommand.Builder builder) {
        if(shouldResetDraftResponse())
            builder.resetResponse(defaultResponse);
    }

    private boolean shouldResetDraftResponse() {
        return Objects.equals(HotelRfpBidStateStatus.RECEIVED, bidStatus) && ( isContactNotMatchingBidType() || isDifferentEmailAddress() );
    }

    private boolean isDifferentEmailAddress() {
        return !newSupplier.emailAddress.equalsIgnoreCase(currentSupplier.emailAddress);
    }

    private void setNewSupplier(HotelRfpSupplierContact supplierContact) {
        newSupplier = supplierContact;
        newSupplierType = supplierContact.getType();
        newSupplierAccountId = Id.valueOf(supplierContact.company.accountId);
    }

    public void setBidId(Id bidId) {
        this.bidId = bidId;
    }

    public void setRfpChainSupport(boolean rfpChainSupport) {
        this.rfpChainSupport = rfpChainSupport;
    }

    public void setCurrentBidType(HotelRfpType currentBidType) {
        this.currentBidType = currentBidType;
    }

    public void setCoverLetterTemplate(String coverLetterTemplate) {
        this.coverLetterTemplate = coverLetterTemplate;
    }

    public void setNamCoverLetterTemplate(String namCoverLetterTemplate) {
        this.namCoverLetterTemplate = namCoverLetterTemplate;
    }

    public void setDefaultResponse(QuestionnaireResponse response) {
        defaultResponse = response;
    }

    public void setCurrentSupplierContact(HotelRfpSupplierContact contact) {
        currentSupplier = contact;
    }

    public void setBidStatus(HotelRfpBidStateStatus status) {
        this.bidStatus = status;
    }

    public void setRfpId(Id id) {
        rfpId = id;
    }

    public void setRfpName(String rfpName) {
        this.rfpName = rfpName;
    }
}
