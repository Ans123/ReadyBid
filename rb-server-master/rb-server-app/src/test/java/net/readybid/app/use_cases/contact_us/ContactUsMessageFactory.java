package net.readybid.app.use_cases.contact_us;

import net.readybid.test_utils.RbRandom;

public class ContactUsMessageFactory {

    private ContactUsMessageFactory () {}

    public static ContactUsMessage random() {
        final String name = RbRandom.alphanumeric(100, false);
        final String emailAddress = RbRandom.alphanumeric(250, false);
        final String message = RbRandom.alphanumeric(1000, false);
        return new ContactUsMessage(name, emailAddress, message);
    }
}
