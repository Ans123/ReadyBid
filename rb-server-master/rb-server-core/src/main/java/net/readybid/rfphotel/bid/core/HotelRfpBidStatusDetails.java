package net.readybid.rfphotel.bid.core;

import net.readybid.user.BasicUserDetails;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class HotelRfpBidStatusDetails {

    public final HotelRfpBidStateStatus status;
    public final String label;
    public final BasicUserDetails by;
    public final Date at;
    public final Map<String, Object> properties;

    HotelRfpBidStatusDetails(HotelRfpBidStateStatus status, String label, BasicUserDetails by, Date at) {
        this(status, label, by, at, null);
    }

    HotelRfpBidStatusDetails(HotelRfpBidStateStatus status, String label, BasicUserDetails by, Date at, Map<String, Object> properties) {
        this.status = status;
        this.label = label;
        this.by = by;
        this.at = at;
        this.properties = properties == null ? null : Collections.unmodifiableMap(properties);
    }
}
