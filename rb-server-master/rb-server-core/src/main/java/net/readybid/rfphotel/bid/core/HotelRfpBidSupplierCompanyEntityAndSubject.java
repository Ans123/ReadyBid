package net.readybid.rfphotel.bid.core;

import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/17/2017.
 *
 */
public interface HotelRfpBidSupplierCompanyEntityAndSubject {
    ObjectId getId();

    ObjectId getSupplierCompanyEntityId();

    String getSubjectId();

    HotelRfpBidStateStatus getStateStatus();
}
