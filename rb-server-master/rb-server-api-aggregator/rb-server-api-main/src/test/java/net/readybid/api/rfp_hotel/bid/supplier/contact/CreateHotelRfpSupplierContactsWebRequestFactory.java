package net.readybid.api.rfp_hotel.bid.supplier.contact;

import net.readybid.test_utils.RbRandom;

import java.util.Arrays;

class CreateHotelRfpSupplierContactsWebRequestFactory {

    public static CreateHotelRfpSupplierContactsWebRequest random() {
        final CreateHotelRfpSupplierContactsWebRequest request = new CreateHotelRfpSupplierContactsWebRequest();

        request.firstName = RbRandom.name();
        request.lastName = RbRandom.name();
        request.emailAddress = RbRandom.emailAddress();
        request.phone = RbRandom.phone();
        request.jobTitle = RbRandom.alphanumeric(100, false);
        request.bids = Arrays.asList(RbRandom.idAsString(), RbRandom.idAsString());

        return request;
    }

    private CreateHotelRfpSupplierContactsWebRequestFactory(){}

}