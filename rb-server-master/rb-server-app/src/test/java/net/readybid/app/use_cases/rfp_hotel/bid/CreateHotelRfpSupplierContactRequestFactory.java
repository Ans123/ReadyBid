package net.readybid.app.use_cases.rfp_hotel.bid;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.test_utils.RbRandom;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateHotelRfpSupplierContactRequestFactory{

    private final static List<EntityType> TYPES =
            Collections.unmodifiableList(Arrays.asList(EntityType.CHAIN, EntityType.HOTEL));

    public static CreateHotelRfpSupplierContactRequest random(EntityType type) {
        final CreateHotelRfpSupplierContactRequest request = random();
        request.type = type;
        return request;
    }

    public static CreateHotelRfpSupplierContactRequest random() {
        final CreateHotelRfpSupplierContactRequest request = new CreateHotelRfpSupplierContactRequest();

        request.type = TYPES.get(RbRandom.randomInt(TYPES.size()));
        request.firstName = RbRandom.name();
        request.lastName = RbRandom.name();
        request.emailAddress = RbRandom.emailAddress();
        request.phone = RbRandom.phone();
        request.jobTitle = RbRandom.alphanumeric(100, false);

        return request;
    }

    private CreateHotelRfpSupplierContactRequestFactory(){}
}