package net.readybid.app.persistence.mongodb.repository;

import net.readybid.app.use_cases.contact_us.CustomRfpMessage;

public interface CustomRfpMessageRepository {
    void insert(CustomRfpMessage customRfpMessage);
}
