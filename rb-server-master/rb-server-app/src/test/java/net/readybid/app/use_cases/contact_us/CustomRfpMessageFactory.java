package net.readybid.app.use_cases.contact_us;

import net.readybid.test_utils.RbRandom;

public class CustomRfpMessageFactory {

    private CustomRfpMessageFactory() {}

    public static CustomRfpMessage random() {
        final String firstName = RbRandom.alphanumeric(100, false);
        final String lastName = RbRandom.alphanumeric(100, false);
        final String emailAddress = RbRandom.alphanumeric(250, false);
        final String phone = RbRandom.alphanumeric(20, false);
        final String company = RbRandom.alphanumeric(100, false);
        final String message = RbRandom.alphanumeric(1000, false);
        return new CustomRfpMessage(firstName, lastName, emailAddress, phone, company, message);
    }
}
