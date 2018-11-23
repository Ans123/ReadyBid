package net.readybid.rfphotel.bid.core;

public enum HotelRfpBidStateStatus {
    CREATED("CREATED", "Bid Not Sent", null),
    SENT("SENT", "Bid Sent", "New Bid"),
    RECEIVED("RECEIVED", "Bid Opened", "Bid Incomplete / Bid Complete"), // supplier label set at State level
    RESPONDED("RESPONDED", "Responded", "Response Sent"),
    NEGOTIATION_SENT("NEGOTIATION_SENT", "Neg. Sent", "Neg. Received"),
    NEGOTIATION_RESPONDED("NEGOTIATION_RESPONDED", "Neg. Received", "Neg. Responded"),
    NEGOTIATION_FINALIZED("NEGOTIATION_FINALIZED", "Neg. Finalized", "Neg. Finalized"),
    WITHDRAWN("WITHDRAWN", "Withdrawn", "Withdrawn"),
    DELETED("DELETED", "Deleted", "Deleted"),
    FINAL_AGREEMENT("FINAL_AGREEMENT", "Final Agreement Sent", "Final Agreement Received");

    public final String id;
    public final String buyerLabel;
    public final String supplierLabel;

    HotelRfpBidStateStatus(String id, String buyerLabel, String supplierLabel) {
        this.id = id;
        this.buyerLabel = buyerLabel;
        this.supplierLabel = supplierLabel;
    }
}