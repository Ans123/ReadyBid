package net.readybid.app.interactors.contact_us.gate;

import net.readybid.app.use_cases.contact_us.ContactUsMessage;

public interface ContactUsMessageStorageManager {
    void insert(ContactUsMessage contactUsMessage);
}
