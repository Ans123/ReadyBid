package net.readybid.api.rfp_hotel.bid.supplier.contact;

import net.readybid.test_utils.RbRandom;

public class SetHotelRfpSupplierContactWebRequestFactory {

    public static SetHotelRfpSupplierContactWebRequest random() {
        final SetHotelRfpSupplierContactWebRequest request = new SetHotelRfpSupplierContactWebRequest();
        request.userAccountId = RbRandom.idAsString();
        return request;
    }
}