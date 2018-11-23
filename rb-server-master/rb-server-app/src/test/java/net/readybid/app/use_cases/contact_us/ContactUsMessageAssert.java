package net.readybid.app.use_cases.contact_us;

import net.readybid.test_utils.RbAbstractAssert;

import java.time.ZonedDateTime;

public class ContactUsMessageAssert extends RbAbstractAssert<ContactUsMessageAssert, ContactUsMessage> {

    public static ContactUsMessageAssert that(ContactUsMessage actual) {
        return new ContactUsMessageAssert(actual);
    }

    private ContactUsMessageAssert(ContactUsMessage actual) {
        super(actual, ContactUsMessageAssert.class);
    }

    public ContactUsMessageAssert hasName(Object expected) {
        assertFieldEquals("name", actual.name, expected);
        return this;
    }

    public ContactUsMessageAssert hasEmailAddress(Object expected) {
        assertFieldEquals("email address", actual.emailAddress, expected);
        return this;
    }

    public ContactUsMessageAssert hasMessage(Object expected) {
        assertFieldEquals("message", actual.message, expected);
        return this;
    }

    public ContactUsMessageAssert hasCreatedAtAfter(ZonedDateTime creationMark) {
        assertAfterInclusive("createdAt", creationMark, actual.createdAt);
        return this;
    }
}
