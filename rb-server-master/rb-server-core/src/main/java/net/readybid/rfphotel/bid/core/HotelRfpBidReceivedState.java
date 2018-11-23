package net.readybid.rfphotel.bid.core;

import net.readybid.user.BasicUserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HotelRfpBidReceivedState implements HotelRfpBidState {

    private final HotelRfpBidStateStatus status = HotelRfpBidStateStatus.RECEIVED;
    private BasicUserDetails buyerStatusBy;
    private Date buyerStatusAt;
    private BasicUserDetails supplierStatusBy;
    private Date supplierStatusAt;
    private long responseErrorsCount;
    private boolean isResponseTouched;

    @Override
    public HotelRfpBidStatusDetails getSupplierStatusDetails() {
        final String label = isResponseTouched ? (isResponseValid() ? "Bid Complete" : "Bid Incomplete") : "New Bid";
        final Map<String, Object> properties = new HashMap<>();
        properties.put("errors", responseErrorsCount);
        properties.put("isTouched", isResponseTouched);
        properties.put("isValid", isResponseValid());
        return new HotelRfpBidStatusDetails(
                status,
                label,
                supplierStatusBy,
                supplierStatusAt,
                properties
        );
    }

    @Override
    public HotelRfpBidStatusDetails getBuyerStatusDetails() {
        return new HotelRfpBidStatusDetails(status, status.buyerLabel, buyerStatusBy, buyerStatusAt);
    }

    public void setBuyerAt(Date date) {
        buyerStatusAt = date;
    }

    public void setBuyerBy(BasicUserDetails user) {
        buyerStatusBy = user;
    }

    public void setSupplierAt(Date date) {
        supplierStatusAt = date;
    }

    public void setSupplierBy(BasicUserDetails user) {
        supplierStatusBy = user;
    }

    public void setResponseTouched(boolean touched) {
        this.isResponseTouched = touched;
    }
    public void setErrorsCount(long errorsCount) {
        this.responseErrorsCount = errorsCount;
    }

    public HotelRfpBidStateStatus getStatus() {
        return status;
    }

    public Date getBuyerAt() {
        return buyerStatusAt;
    }

    public BasicUserDetails getBuyerBy() {
        return buyerStatusBy;
    }
    public Date getSupplierAt() {
        return supplierStatusAt;
    }

    public BasicUserDetails getSupplierBy() {
        return supplierStatusBy;
    }

    public long getErrorsCount() {
        return responseErrorsCount;
    }

    private boolean isResponseValid() {
        return responseErrorsCount == 0;
    }

    public boolean isResponseTouched() {
        return isResponseTouched;
    }
}
