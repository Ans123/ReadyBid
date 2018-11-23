package net.readybid.api.contact_us;

import net.readybid.test_utils.RbRandom;

public class CustomRfpMessageRequestFactory {
    public static CustomRfpMessageRequest random() {
        final CustomRfpMessageRequest request = new CustomRfpMessageRequest();

        request.firstName = RbRandom.alphanumeric(50, false);
        request.lastName = RbRandom.alphanumeric(50, false);
        request.emailAddress = RbRandom.emailAddress();
        request.phone = RbRandom.alphanumeric(20, false);
        request.company = RbRandom.alphanumeric(100, false);
        request.message = RbRandom.alphanumeric(10000, false);

        return request;
    }
}
