package net.readybid.app.use_cases.contact_us;

import java.time.ZonedDateTime;

public final class ContactUsMessage {

    public final String name;
    public final String emailAddress;
    public final String message;
    public final ZonedDateTime createdAt;

    public ContactUsMessage(String name, String emailAddress, String message){
        this.name = name;
        this.emailAddress = emailAddress;
        this.message = message;
        createdAt = ZonedDateTime.now();
    }
}
