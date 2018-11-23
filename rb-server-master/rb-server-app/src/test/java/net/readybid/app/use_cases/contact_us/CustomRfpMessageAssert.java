package net.readybid.app.use_cases.contact_us;

import net.readybid.test_utils.RbAbstractAssert;

import java.time.ZonedDateTime;

public class CustomRfpMessageAssert extends RbAbstractAssert<CustomRfpMessageAssert, CustomRfpMessage> {

    public static CustomRfpMessageAssert that(CustomRfpMessage actual) {
        return new CustomRfpMessageAssert(actual);
    }

    private CustomRfpMessageAssert(CustomRfpMessage actual) {
        super(actual, CustomRfpMessageAssert.class);
    }

    public CustomRfpMessageAssert hasFirstName(Object expected) {
        assertFieldEquals("first name", actual.firstName, expected);
        return this;
    }

    public CustomRfpMessageAssert hasLastName(Object expected) {
        assertFieldEquals("last name", actual.lastName, expected);
        return this;
    }

    public CustomRfpMessageAssert hasEmailAddress(Object expected) {
        assertFieldEquals("email address", actual.emailAddress, expected);
        return this;
    }

    public CustomRfpMessageAssert hasPhone(Object expected) {
        assertFieldEquals("phone", actual.phone, expected);
        return this;
    }

    public CustomRfpMessageAssert hasCompany(Object expected) {
        assertFieldEquals("company", actual.company, expected);
        return this;
    }

    public CustomRfpMessageAssert hasMessage(Object expected) {
        assertFieldEquals("message", actual.message, expected);
        return this;
    }

    public CustomRfpMessageAssert hasCreatedAtAfter(ZonedDateTime creationMark) {
        assertAfterInclusive("createdAt", creationMark, actual.createdAt);
        return this;
    }
}
