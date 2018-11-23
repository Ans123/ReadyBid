package net.readybid.app.entities.rfp_hotel.bid;

import java.util.Collections;
import java.util.Map;

import net.readybid.rfphotel.bid.core.HotelRfpBidState;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.bid.core.HotelRfpBidStatusDetails;
import net.readybid.utils.RbMapUtils;

// todo: questionnaire response??? for Groups
public class HotelRfpBidQueryView {

    public final String $bidId;
    public final String $rfpId;
    public final boolean $rfpChainSupport;
    public final HotelRfpBidStateStatus $status;
    public final String $hotelId;
    public final String $chainId;
    public final Map<String, Object> $supplierContact;

    public final String _id;
    public final Map<String, Object> rfp;
    public final Map<String, Object> subject;
    public final Map<String, Object> buyer;
    public final Map<String, Object> supplier;
    public final Map<String, Object> analytics;
    public final Map<String, Object> offer;
    public final HotelRfpBidStatusDetails state;
    public String $sabrePropcode;
    
    /*
    @Autowired
    HotelLoader hotelLoader;*/

    public HotelRfpBidQueryView(Builder builder, HotelRfpBidStatusDetails statusDetails) {
        _id = builder._id;
        rfp = createUnmodifiableMap(builder.rfp);
        subject = createUnmodifiableMap(builder.subject);
        buyer = createUnmodifiableMap(builder.buyer);
        supplier = createUnmodifiableMap(builder.supplier);
        analytics = createUnmodifiableMap(builder.analytics);
        offer = createUnmodifiableMap(builder.offer);
        state = statusDetails;

        $bidId = _id;
        $rfpId = get(rfp, "_id");
        $rfpChainSupport = Boolean.valueOf(getOrDefault(rfp, "specifications.chainSupport", "false"));
        $hotelId = get(supplier, "company.entityId");
        $chainId = get(supplier, "company.chain.master._id");
        $supplierContact = createUnmodifiableMap(getMap(supplier, "contact"));
        $status = state == null ? null : statusDetails.status;
        $sabrePropcode = "";
    }

    private Map<String, Object> createUnmodifiableMap(Map<String, Object> map) {
        return map == null ? null : Collections.unmodifiableMap(map);
    }

    private String get(Map<String, Object> map, String path) {
        final Object o = RbMapUtils.getObject(map, path);
        return o == null ? null : String.valueOf(o);
    }

    private String getOrDefault(Map<String, Object> map, String path, String defaultValue) {
        final String v = get(map, path);
        return v == null ? defaultValue : v;
    }

    private Map<String, Object> getMap(Map<String, Object> map, String path) {
        final Object o = RbMapUtils.getObject(map, path);
        return (o instanceof Map) ? (Map) o : null;
    }

    public static class Builder {
        public String _id;
        public Map<String,Object> rfp;
        public Map<String, Object> subject;
        public Map<String, Object> buyer;
        public Map<String, Object> supplier;
        public Map<String, Object> analytics;
        public Map<String, Object> offer;
        public HotelRfpBidState state;
        public String sabrePropcode;

        public HotelRfpBidQueryView buildSupplierView() {
            return new HotelRfpBidQueryView(this, state == null ? null : state.getSupplierStatusDetails());
        }

        public HotelRfpBidQueryView buildBuyerView() {
            return new HotelRfpBidQueryView(this, state == null ? null : state.getBuyerStatusDetails());
        }

        public Builder setId(String bidId) {
            _id = bidId;
            return this;
        }

        public Builder setState(HotelRfpBidState hotelRfpBidState) {
            this.state = hotelRfpBidState;
            return this;
        }
    }
}
