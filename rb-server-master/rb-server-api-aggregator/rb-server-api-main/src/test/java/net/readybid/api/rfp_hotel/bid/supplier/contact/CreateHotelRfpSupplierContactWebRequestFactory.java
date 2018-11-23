package net.readybid.api.rfp_hotel.bid.supplier.contact;

import net.readybid.test_utils.RbRandom;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateHotelRfpSupplierContactWebRequestFactory {

    private final static List<String> TYPES = Collections.unmodifiableList(Arrays.asList("CHAIN", "HOTEL"));

    public static CreateHotelRfpSupplierContactWebRequest random(String type) {
        final CreateHotelRfpSupplierContactWebRequest request = random();
        request.accountType = type;
        return request;
    }

    public static CreateHotelRfpSupplierContactWebRequest random() {
        final CreateHotelRfpSupplierContactWebRequest request = new CreateHotelRfpSupplierContactWebRequest();

        request.accountType = TYPES.get(RbRandom.randomInt(TYPES.size()));
        request.firstName = RbRandom.name();
        request.lastName = RbRandom.name();
        request.emailAddress = RbRandom.emailAddress();
        request.phone = RbRandom.phone();
        request.jobTitle = RbRandom.alphanumeric(100, false);

        return request;
    }

    private CreateHotelRfpSupplierContactWebRequestFactory(){}
}
