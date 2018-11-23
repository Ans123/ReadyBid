package net.readybid.api.contact_us;

import net.readybid.test_utils.RbRandom;

public class ContactUsRequestFactory {

    public static ContactUsRequest random() {
        final ContactUsRequest request = new ContactUsRequest();

        request.name = RbRandom.alphanumeric(100, false);
        request.emailAddress = RbRandom.emailAddress();
        request.message = RbRandom.alphanumeric(1000, false);

        return request;
    }
}
