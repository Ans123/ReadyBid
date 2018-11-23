package net.readybid.app.persistence.mongodb.app.rfp_hotel.bid;

import net.readybid.app.persistence.HotelRfpBidQueryAccessControl;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfphotel.bid.web.BidsQueryRequest;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;
import static net.readybid.mongodb.RbMongoFilters.include;
import static net.readybid.mongodb.RbMongoFilters.oid;

/**
 * Created by DejanK on 1/17/2017.
 *
 */

class HotelRfpBidQueryFactory {

    private static final List<String> KEY_FIELDS = Arrays.asList(
            HotelRfpBidCollection.ID,
            HotelRfpBidCollection.RFP_ID,
            HotelRfpBidCollection.RFP_CHAIN_SUPPORT,
            HotelRfpBidCollection.SUPPLIER_COMPANY_ENTITY_ID,
            HotelRfpBidCollection.SUPPLIER_COMPANY_CHAIN_MASTER_ID,
            HotelRfpBidCollection.SUPPLIER_CONTACT,
            HotelRfpBidCollection.STATE);

    HotelRfpBidManagerViewQuery createQuery(BidsQueryRequest bidsQueryRequest, AuthenticatedUser user) {
        final Bson filter = HotelRfpBidQueryAccessControl.applyPermissionsFilter(createFilter(bidsQueryRequest.filter), user);
        final Bson projection = createProjection(bidsQueryRequest.fields);
        return new HotelRfpBidManagerViewQuery(filter, projection);
    }

    private Bson parseFilterValue(String key, Object val) {
        if(val instanceof Map){
            return parseMapFilterValue(key, (Map) val);
        } else if (val instanceof String){
            return eq(key, parseStringFilterValue((String) val));
        }
        return null;
    }

    private Object parseStringFilterValue(String val) {
        return ObjectId.isValid(val) ? oid(val) : val;
    }

    private Bson parseMapFilterValue(String key, Map val) {
        for(Object mapKey : val.keySet()){
            switch (String.valueOf(mapKey)){
                case "$eq": {
                    final Object mapVal = val.get(key);
                    if(mapVal instanceof String){
                        return eq(key, parseStringFilterValue((String) mapVal));
                    } else {
                        return eq(key, val);
                    }
                }
                case "$ne": {
                    final Object mapVal = val.get(mapKey);
                    if(mapVal instanceof String){
                        return ne(key, parseStringFilterValue((String) mapVal));
                    } else {
                        return ne(key, val);
                    }
                }
            }
        }
        return null;
    }

    public Bson createProjection(List<String> requestFields) {
        final Set<String> fields = requestFields.stream()
                .filter(f -> !f.startsWith("state"))
                .collect(Collectors.toSet());
        fields.addAll(HotelRfpBidQueryFactory.KEY_FIELDS);
        return include(fields);
    }

    /**
     * Examples:
     * {counter: 5 }} => and($eq("counter", 5))
     * {rfpId: "validOID"} => and($eq("rfpId", oid("validOID"))
     * {rfpId: "notValidOID"} => and($eq("rfpId", "notValidOID"))
     * {rfpId: { $eq: "validOID" }} => and($eq("rfpId", oid("validOID")))
     * {rfpId: { $eq: "notValidOID" }} => and($eq("rfpId", "notValidOID"))
     * {counter: { $eq: 5 }} => and($eq("counter", 5))
     *
     * {rfpId: { $ne: "validOID" }} => and($ne("rfpId", oid("validOID")))
     * {rfpId: { $ne: "notValidOID" }} => and($ne("rfpId", "notValidOID"))
     * {counter: { $ne: 5 }} => and($ne("counter", 5))
     *
     *
     */
    private Bson createFilter(Map<String, ?> requestFilter) {
        final List<Bson> filters = new ArrayList<>();
        for(String key : requestFilter.keySet()){
            final Bson filter = parseFilterValue(key, requestFilter.get(key));
            if (filter != null) filters.add(filter);
        }
        return filters.size() == 0 ? null : and(filters);
    }
}
