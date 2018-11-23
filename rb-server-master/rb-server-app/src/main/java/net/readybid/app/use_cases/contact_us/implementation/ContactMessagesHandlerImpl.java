package net.readybid.app.use_cases.contact_us.implementation;

import net.readybid.app.entities.email.EmailTemplate;
import net.readybid.app.interactors.contact_us.gate.ContactUsMessageStorageManager;
import net.readybid.app.interactors.contact_us.gate.CustomRfpMessageStorageManager;
import net.readybid.app.interactors.email.EmailService;
import net.readybid.app.interactors.email.EmailTemplateTools;
import net.readybid.app.use_cases.contact_us.ContactMessagesHandler;
import net.readybid.app.use_cases.contact_us.ContactUsMessage;
import net.readybid.app.use_cases.contact_us.CustomRfpMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
class ContactMessagesHandlerImpl implements ContactMessagesHandler {

    private final ContactUsMessageStorageManager contactUsStorage;
    private final CustomRfpMessageStorageManager customRfpMessageStorage;
    private final EmailService emailService;
    private final String receiver;

    @Autowired
    ContactMessagesHandlerImpl(
            ContactUsMessageStorageManager contactUsStorage,
            CustomRfpMessageStorageManager customRfpMessageStorage,
            EmailService emailService,
            Environment environment
    ) {
        this.contactUsStorage = contactUsStorage;
        this.customRfpMessageStorage = customRfpMessageStorage;
        this.emailService = emailService;
        receiver = environment.getRequiredProperty("email.receiver.contactUs");
    }

    @Override
    public void handleContactUsMessage(ContactUsMessage contactUsMessage) {
        contactUsStorage.insert(contactUsMessage);
        emailService.send(createEmail(contactUsMessage));
    }

    private EmailTemplate createEmail(ContactUsMessage contactUsMessage) {
        final String subject = "ReadyBid: New Contact Us Message";
        final ContactUsEmailTemplate emailTemplate = new ContactUsEmailTemplate(receiver, subject);
        emailTemplate.to = "Colleague";
        emailTemplate.introduction = "New Contact Us Message has been received with following details:";
        emailTemplate.messageAuthorName = contactUsMessage.name;
        emailTemplate.messageAuthorEmailAddress = contactUsMessage.emailAddress;
        emailTemplate.messageCreatedAt = contactUsMessage.createdAt;
        emailTemplate.messageText = contactUsMessage.message;

        return emailTemplate;
    }

    @Override
    public void handleCustomRfpMessage(CustomRfpMessage customRfpMessage) {
        customRfpMessageStorage.insert(customRfpMessage);
        emailService.send(createEmail(customRfpMessage));
    }

    private EmailTemplate createEmail(CustomRfpMessage message) {
        final String subject = String.format("ReadyBid: New Custom RFP Message from %s", message.company);
        final ContactUsEmailTemplate emailTemplate = new ContactUsEmailTemplate(receiver, subject);
        emailTemplate.to = "Colleague";
        emailTemplate.introduction = "New Custom RFP Message has been received with following details:";
        emailTemplate.messageSubject = message.company;
        emailTemplate.messageAuthorName = String.format("%s %s", message.lastName, message.firstName);
        emailTemplate.messageAuthorEmailAddress = message.emailAddress;
        emailTemplate.messageAuthorPhone = message.phone;
        emailTemplate.messageCreatedAt = message.createdAt;
        emailTemplate.messageText = message.message;

        return emailTemplate;
    }

    private class ContactUsEmailTemplate implements EmailTemplate {

        private static final String HTML_TEMPLATE = "/templates/contact-us-email.html";

        private String to = "NA";
        private String introduction = "NA";
        private String messageSubject = "NA";
        private String messageAuthorName = "NA";
        private String messageAuthorEmailAddress = "NA";
        private String messageAuthorPhone = "NA";
        private ZonedDateTime messageCreatedAt = null;
        private String messageText = "NA";

        private final InternetAddress receiver;
        private final String emailSubject;

        private ContactUsEmailTemplate(String receiverEmailAddress, String subject) {
            this.receiver = EmailTemplateTools.createInternetAddress(receiverEmailAddress);
            this.emailSubject = subject;
        }

        @Override
        public String getId() {
            return messageAuthorEmailAddress;
        }

        @Override
        public InternetAddress getReceiver() {
            return receiver;
        }

        @Override
        public InternetAddress[] getCC() {
            return null;
        }

        @Override
        public String getSubject() {
            return emailSubject;
        }

        @Override
        public String getHtmlTemplateName() {
            return HTML_TEMPLATE;
        }

        @Override
        public Map<String, String> getModel() {
            final Map<String, String> model = new HashMap<>();
            model.put("FIRST_NAME", to);
            model.put("MESSAGE", introduction);
            model.put("SUBJECT", messageSubject);
            model.put("FROM", messageAuthorName);
            model.put("EMAIL", messageAuthorEmailAddress);
            model.put("PHONE", messageAuthorPhone);
            model.put("CREATED_AT", DateTimeFormatter.RFC_1123_DATE_TIME.format(messageCreatedAt));
            model.put("TEXT", messageText);

            return Collections.unmodifiableMap(model);
        }
    }
}
