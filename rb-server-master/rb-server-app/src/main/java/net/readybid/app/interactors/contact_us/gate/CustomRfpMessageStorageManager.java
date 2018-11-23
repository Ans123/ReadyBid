package net.readybid.app.interactors.contact_us.gate;

import net.readybid.app.use_cases.contact_us.CustomRfpMessage;

public interface CustomRfpMessageStorageManager {
    void insert(CustomRfpMessage customRfpMessage);
}
