package net.readybid.app.entities.rfp_hotel;

import net.readybid.test_utils.RbAbstractAssert;

public class HotelRfpRepresentativeAssert  extends RbAbstractAssert<HotelRfpRepresentativeAssert, HotelRfpRepresentative> {

    public static HotelRfpRepresentativeAssert that(HotelRfpRepresentative actual) {
        return new HotelRfpRepresentativeAssert(actual);
    }

    private HotelRfpRepresentativeAssert(HotelRfpRepresentative actual) {
        super(actual, HotelRfpRepresentativeAssert.class);
    }

    public HotelRfpRepresentativeAssert hasId(Object expected) {
        assertFieldEquals("id", expected, actual.id);
        return this;
    }

    public HotelRfpRepresentativeAssert hasUserId(Object expected) {
        assertFieldEquals("user id", expected, actual.userId);
        return this;
    }

    public HotelRfpRepresentativeAssert hasAccountId(Object expected) {
        assertFieldEquals("account id", expected, actual.accountId);
        return this;
    }

    public HotelRfpRepresentativeAssert hasEntityId(Object expected) {
        assertFieldEquals("entity id", expected, actual.entityId);
        return this;
    }

    public HotelRfpRepresentativeAssert hasAccountName(Object expected) {
        assertFieldEquals("account name", expected, actual.accountName);
        return this;
    }

    public HotelRfpRepresentativeAssert hasAccountType(Object expected) {
        assertFieldEquals("account type", expected, actual.accountType);
        return this;
    }

    public HotelRfpRepresentativeAssert hasFirstName(Object expected) {
        assertFieldEquals("first name", expected, actual.firstName);
        return this;
    }

    public HotelRfpRepresentativeAssert hasLastName(Object expected) {
        assertFieldEquals("last name", expected, actual.lastName);
        return this;
    }

    public HotelRfpRepresentativeAssert hasEmailAddress(Object expected) {
        assertFieldEquals("email address", expected, actual.emailAddress);
        return this;
    }

    public HotelRfpRepresentativeAssert hasPhone(Object expected) {
        assertFieldEquals("phone", expected, actual.phone);
        return this;
    }

    public HotelRfpRepresentativeAssert hasProfilePicture(Object expected) {
        assertFieldEquals("profile picture", expected, actual.profilePicture);
        return this;
    }

    public HotelRfpRepresentativeAssert hasJobTitle(Object expected) {
        assertFieldEquals("job title", expected, actual.jobTitle);
        return this;
    }

    public HotelRfpRepresentativeAssert isUser(Object expected) {
        assertFieldEquals("is user", expected, actual.isUser);
        return this;
    }
}