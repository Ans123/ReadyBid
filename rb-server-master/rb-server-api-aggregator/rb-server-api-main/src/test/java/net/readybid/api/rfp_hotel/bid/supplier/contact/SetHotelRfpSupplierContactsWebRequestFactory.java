package net.readybid.api.rfp_hotel.bid.supplier.contact;

import net.readybid.test_utils.RbRandom;

import java.util.Arrays;

public class SetHotelRfpSupplierContactsWebRequestFactory {

    public static SetHotelRfpSupplierContactsWebRequest random() {
        final SetHotelRfpSupplierContactsWebRequest request = new SetHotelRfpSupplierContactsWebRequest();
        request.userAccountId = RbRandom.idAsString();
        request.bids = Arrays.asList(RbRandom.idAsString(), RbRandom.idAsString());

        return request;
    }
}