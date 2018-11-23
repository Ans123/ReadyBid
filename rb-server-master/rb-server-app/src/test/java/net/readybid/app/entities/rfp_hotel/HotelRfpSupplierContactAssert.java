package net.readybid.app.entities.rfp_hotel;

import net.readybid.test_utils.RbAbstractAssert;
import org.bson.types.ObjectId;

import java.util.function.Consumer;

public class HotelRfpSupplierContactAssert extends RbAbstractAssert<HotelRfpSupplierContactAssert, HotelRfpSupplierContact> {

    public static HotelRfpSupplierContactAssert that(HotelRfpSupplierContact actual) {
        return new HotelRfpSupplierContactAssert(actual);
    }

    private HotelRfpSupplierContactAssert(HotelRfpSupplierContact actual) {
        super(actual, HotelRfpSupplierContactAssert.class);
    }

    public HotelRfpSupplierContactAssert hasIdCreatedAfter(Object expected) {
        isNotNull();
        final ObjectId expectedId = new ObjectId(String.valueOf(expected));
        final ObjectId actualId = new ObjectId(String.valueOf(actual.id));

        if (expectedId.compareTo(actualId) > 0) {
            failWithMessage("Expected id to be before <%s> but was <%s>", expected, actual.id);
        }
        return this;
    }

    public HotelRfpSupplierContactAssert hasFirstName(Object expected) {
        assertFieldEquals("first name", actual.firstName, expected);
        return this;
    }

    public HotelRfpSupplierContactAssert hasLastName(Object expected) {
        assertFieldEquals("last name", actual.lastName, expected);
        return this;
    }

    public HotelRfpSupplierContactAssert hasEmailAddress(Object expected) {
        assertFieldEquals("email address", actual.emailAddress, expected);
        return this;
    }

    public HotelRfpSupplierContactAssert hasPhone(Object expected) {
        assertFieldEquals("phone", actual.phone, expected);
        return this;
    }

    public HotelRfpSupplierContactAssert hasJobTitle(Object expected) {
        assertFieldEquals("job title", actual.jobTitle, expected);
        return this;
    }

    public HotelRfpSupplierContactAssert hasProfilePicture(Object expected) {
        assertFieldEquals("profile picture", actual.profilePicture, expected);
        return this;
    }

    public HotelRfpSupplierContactAssert hasIsUser(Object expected) {
        assertFieldEquals("is user", actual.isUser, expected);
        return this;
    }

    public HotelRfpSupplierContactAssert hasCompany(Consumer<HotelRfpContactAccountAssert> consumer) {
        consumer.accept(HotelRfpContactAccountAssert.that(actual.company));
        return this;
    }
}