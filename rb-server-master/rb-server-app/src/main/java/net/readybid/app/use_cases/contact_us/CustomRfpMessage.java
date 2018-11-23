package net.readybid.app.use_cases.contact_us;

import java.time.ZonedDateTime;

public final class CustomRfpMessage {

    public final String firstName;
    public final String lastName;
    public final String emailAddress;
    public final String phone;
    public final String company;
    public final String message;
    public final ZonedDateTime createdAt;

    public CustomRfpMessage(String firstName, String lastName, String emailAddress, String phone, String company, String message) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.company = company;
        this.message = message;
        createdAt = ZonedDateTime.now();
    }
}
