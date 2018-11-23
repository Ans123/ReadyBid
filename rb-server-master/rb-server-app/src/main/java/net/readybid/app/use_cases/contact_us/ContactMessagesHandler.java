package net.readybid.app.use_cases.contact_us;

public interface ContactMessagesHandler {
    void handleContactUsMessage(ContactUsMessage contactUsMessage);

    void handleCustomRfpMessage(CustomRfpMessage customRfpMessage);
}
