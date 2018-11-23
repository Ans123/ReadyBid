package net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier;

import net.readybid.app.core.entities.rfp.QuestionnaireResponse;
import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.entities.Id;
import net.readybid.rfp.specifications.HotelRfpType;
import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;

import java.time.LocalDate;

public class HotelRfpBidSetSupplierContactCommand {

    public final Id bidId;
    public final HotelRfpBidStateStatus currentBidStatus;
    public final boolean shouldUpdateContact;
    public final HotelRfpSupplierContact supplierContact;
    public final boolean shouldChangeBidType;
    public final HotelRfpType bidType;
    public final String coverLetter;
    public final boolean shouldResetResponse;
    public final QuestionnaireResponse response;
    public final boolean shouldSendBid;
    public final HotelRfpBidState bidState;
    public final LocalDate sentDate;

    private HotelRfpBidSetSupplierContactCommand(Builder builder){
        bidId = builder.bidId;
        currentBidStatus = builder.currentBidStatus;
        shouldUpdateContact = builder.shouldUpdateContact;
        supplierContact = builder.supplierContact;
        shouldChangeBidType = builder.shouldChangeBidType;
        bidType = shouldChangeBidType ? builder.bidType : null;
        coverLetter = shouldChangeBidType ? builder.coverLetterTemplate : null;
        shouldResetResponse = builder.shouldResetResponse;
        response = shouldResetResponse ? builder.defaultResponse : null;
        shouldSendBid = builder.shouldSendBid;
        bidState = shouldSendBid ? builder.bidState : null;
        sentDate = shouldSendBid ? builder.sentDate : null;
    }

    public static class Builder {
        private final Id bidId;
        private final HotelRfpBidStateStatus currentBidStatus;
        private final boolean shouldUpdateContact;
        private final HotelRfpSupplierContact supplierContact;
        private boolean shouldChangeBidType;
        private HotelRfpType bidType;
        private String coverLetterTemplate;
        private QuestionnaireResponse defaultResponse;
        private boolean shouldSendBid;
        private HotelRfpBidState bidState;
        private LocalDate sentDate;
        private boolean shouldResetResponse;

        public Builder(Id bidId, HotelRfpBidStateStatus currentBidStatus, HotelRfpSupplierContact supplierContact) {
            this.bidId = bidId;
            this.currentBidStatus = currentBidStatus;
            this.shouldUpdateContact = true;
            this.supplierContact = supplierContact;
        }

        public Builder(Id bidId, HotelRfpBidStateStatus status) {
            this.bidId = bidId;
            this.currentBidStatus = status;
            this.shouldUpdateContact = false;
            this.supplierContact = null;
        }

        public void changeBidType(HotelRfpType bidType, String coverLetterTemplate) {
            shouldChangeBidType = true;
            this.bidType = bidType;
            this.coverLetterTemplate = coverLetterTemplate;
        }

        public void resetResponse(QuestionnaireResponse defaultResponse) {
            shouldResetResponse = true;
            this.defaultResponse = defaultResponse;
        }

        public void setSendBidData(HotelRfpBidState bidState, LocalDate sentDate) {
            shouldSendBid = true;
            this.bidState = bidState;
            this.sentDate = sentDate;
        }

        public HotelRfpBidSetSupplierContactCommand build() {
            return new HotelRfpBidSetSupplierContactCommand(this);
        }
    }
}
