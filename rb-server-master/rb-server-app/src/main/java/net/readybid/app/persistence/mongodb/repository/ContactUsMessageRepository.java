package net.readybid.app.persistence.mongodb.repository;

import net.readybid.app.use_cases.contact_us.ContactUsMessage;

public interface ContactUsMessageRepository {
    void insert(ContactUsMessage contactUsMessage);
}
