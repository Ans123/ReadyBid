package net.readybid.rfphotel.bid.core;

import net.readybid.user.BasicUserDetails;

import java.util.Date;

public class HotelRfpBidSimpleState implements HotelRfpBidState {

    private HotelRfpBidStateStatus status;
    private BasicUserDetails by;
    private Date at;

    @Override
    public HotelRfpBidStatusDetails getSupplierStatusDetails() {
        return new HotelRfpBidStatusDetails(status, status.supplierLabel, by, at);
    }

    @Override
    public HotelRfpBidStatusDetails getBuyerStatusDetails() {
        return new HotelRfpBidStatusDetails(status, status.buyerLabel, by, at);
    }

    public void setStatus(HotelRfpBidStateStatus status) {
        this.status = status;
    }

    public void setAt(Date date) {
        this.at = date;
    }

    public void setBy(BasicUserDetails userDetails) {
        this.by = userDetails;
    }

    public Date getAt() {
        return at;
    }

    public HotelRfpBidStateStatus getStatus() {
        return status;
    }

    public BasicUserDetails getBy() {
        return by;
    }
}
