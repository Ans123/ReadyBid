package net.readybid.auth.invitation;

import net.readybid.test_utils.RbAbstractAssert;

public class InvitationImplAssert extends RbAbstractAssert<InvitationImplAssert, InvitationImpl> {

    public static InvitationImplAssert that(InvitationImpl actual) {
        return new InvitationImplAssert(actual);
    }

    private InvitationImplAssert(InvitationImpl actual) {
        super(actual, InvitationImplAssert.class);
    }

    public InvitationImplAssert hasId(Object expected) {
        assertFieldEquals("id", String.valueOf(expected), String.valueOf(actual.getId()));
        return this;
    }

    public InvitationImplAssert hasFirstName(Object expected) {
        assertFieldEquals("first name", expected, actual.getFirstName());
        return this;
    }

    public InvitationImplAssert hasLastName(Object expected) {
        assertFieldEquals("last name", expected, actual.getLastName());
        return this;
    }

    public InvitationImplAssert hasEmailAddress(Object expected) {
        assertFieldEquals("email address", expected, actual.getEmailAddress());
        return this;
    }

    public InvitationImplAssert hasPhone(Object expected) {
        assertFieldEquals("phone", expected, actual.getPhone());
        return this;
    }

    public InvitationImplAssert hasJobTitle(Object expected) {
        assertFieldEquals("job title", expected, actual.getJobTitle());
        return this;
    }

    public InvitationImplAssert hasAccountId(Object expected) {
        assertFieldEquals("account id", String.valueOf(expected), String.valueOf(actual.getAccountId()));
        return this;
    }

    public InvitationImplAssert hasAccountName(Object expected) {
        assertFieldEquals("account name", expected, actual.getAccountName());
        return this;
    }

    public InvitationImplAssert hasTargetId(Object expected) {
        assertFieldEquals("target id", String.valueOf(expected), String.valueOf(actual.getTargetId()));
        return this;
    }
}