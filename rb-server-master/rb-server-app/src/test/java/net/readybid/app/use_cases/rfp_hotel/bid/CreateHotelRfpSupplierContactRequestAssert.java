package net.readybid.app.use_cases.rfp_hotel.bid;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.test_utils.RbAbstractAssert;

public class CreateHotelRfpSupplierContactRequestAssert extends RbAbstractAssert<CreateHotelRfpSupplierContactRequestAssert, CreateHotelRfpSupplierContactRequest> {

    public static CreateHotelRfpSupplierContactRequestAssert that(CreateHotelRfpSupplierContactRequest actual) {
        return new CreateHotelRfpSupplierContactRequestAssert(actual);
    }

    private CreateHotelRfpSupplierContactRequestAssert(CreateHotelRfpSupplierContactRequest actual) {
        super(actual, CreateHotelRfpSupplierContactRequestAssert.class);
    }

    public CreateHotelRfpSupplierContactRequestAssert hasType(Object expected) {
        assertFieldEquals("type", actual.type, EntityType.valueOf(String.valueOf(expected)));
        return this;
    }

    public CreateHotelRfpSupplierContactRequestAssert hasFirstName(Object expected) {
        assertFieldEquals("first name", actual.firstName, expected);
        return this;
    }

    public CreateHotelRfpSupplierContactRequestAssert hasLastName(Object expected) {
        assertFieldEquals("last name", actual.lastName, expected);
        return this;
    }

    public CreateHotelRfpSupplierContactRequestAssert hasEmailAddress(Object expected) {
        assertFieldEquals("email address", actual.emailAddress, expected);
        return this;
    }

    public CreateHotelRfpSupplierContactRequestAssert hasPhone(Object expected) {
        assertFieldEquals("phone", actual.phone, expected);
        return this;
    }

    public CreateHotelRfpSupplierContactRequestAssert hasJobTitle(Object expected) {
        assertFieldEquals("job title", actual.jobTitle, expected);
        return this;
    }
}