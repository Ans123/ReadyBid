package net.readybid.app.use_cases.contact_us.implementation;

import net.readybid.app.entities.email.EmailTemplate;
import net.readybid.app.entities.email.EmailTemplateAssert;
import net.readybid.app.interactors.contact_us.gate.ContactUsMessageStorageManager;
import net.readybid.app.interactors.contact_us.gate.CustomRfpMessageStorageManager;
import net.readybid.app.interactors.email.EmailService;
import net.readybid.app.use_cases.contact_us.ContactUsMessage;
import net.readybid.app.use_cases.contact_us.ContactUsMessageFactory;
import net.readybid.app.use_cases.contact_us.CustomRfpMessage;
import net.readybid.app.use_cases.contact_us.CustomRfpMessageFactory;
import net.readybid.templates.TemplateService;
import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.env.Environment;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@DisplayName("ContactUsMessageHandler ")
class ContactMessagesHandlerImplTest {

    private ContactMessagesHandlerImpl sut;

    private ContactUsMessageStorageManager contactUsMessageStorage;
    private CustomRfpMessageStorageManager customRfpMessageStorage;
    private EmailService emailService;

    private String receiverEmailAddress;

    @BeforeEach
    void setUp(){
        final Environment environment = mock(Environment.class);

        contactUsMessageStorage = mock(ContactUsMessageStorageManager.class);
        customRfpMessageStorage = mock(CustomRfpMessageStorageManager.class);
        emailService = mock(EmailService.class);

        receiverEmailAddress = RbRandom.emailAddress();

        doReturn(receiverEmailAddress).when(environment).getRequiredProperty("email.receiver.contactUs");

        sut = new ContactMessagesHandlerImpl(contactUsMessageStorage, customRfpMessageStorage, emailService, environment);
    }

    @Nested
    @DisplayName("When new Contact Us Message is received ")
    class ReceiveContactUsMessageTest {

        final Consumer<ContactUsMessage> mut =
                contactUsMessage -> sut.handleContactUsMessage(contactUsMessage);

        @Test
        @DisplayName("message should be saved to the database")
        void saveMessageTest() {
            final ContactUsMessage message = ContactUsMessageFactory.random();

            mut.accept(message);

            verify(contactUsMessageStorage, times(1)).insert(same(message));
        }

        @Test
        @DisplayName("notify Contact Messages Person of the new message by email")
        void sendEmailTest() {
            final ArgumentCaptor<EmailTemplate> emailTemplateCaptor = ArgumentCaptor.forClass(EmailTemplate.class);
            final ContactUsMessage message = ContactUsMessageFactory.random();

            mut.accept(message);

            verify(emailService, times(1)).send(emailTemplateCaptor.capture());
            EmailTemplateAssert.that(emailTemplateCaptor.getValue())
                    .hasReceiverEmailAddress(receiverEmailAddress)
                    .hasReceiverName(receiverEmailAddress)
                    .hasSubject("ReadyBid: New Contact Us Message")
                    .hasEmptyCC()
                    .hasHtmlTemplate(TemplateService.CONTACT_US_TEMPLATE)
                    .modelAssert(model -> model
                            .contains("FIRST_NAME", "Colleague")
                            .contains("MESSAGE", "New Contact Us Message has been received with following details:")
                            .contains("SUBJECT", "NA")
                            .contains("FROM", message.name)
                            .contains("EMAIL", message.emailAddress)
                            .contains("PHONE", "NA")
                            .contains("CREATED_AT", DateTimeFormatter.RFC_1123_DATE_TIME.format(message.createdAt))
                            .contains("TEXT", message.message));
        }
    }

    @Nested
    @DisplayName("When new Custom Rfp Message is received ")
    class ReceiveCustomRfpMessageTest {

        final Consumer<CustomRfpMessage> mut =
                message -> sut.handleCustomRfpMessage(message);

        @Test
        @DisplayName("message should be saved to the database")
        void saveMessageTest() {
            final CustomRfpMessage message = CustomRfpMessageFactory.random();

            mut.accept(message);

            verify(customRfpMessageStorage, times(1)).insert(same(message));
        }

        @Test
        @DisplayName("notify Custom RFP Messages Person of the new message by email")
        void sendEmailTest() {
            final ArgumentCaptor<EmailTemplate> emailTemplateCaptor = ArgumentCaptor.forClass(EmailTemplate.class);
            final CustomRfpMessage message = CustomRfpMessageFactory.random();

            mut.accept(message);

            verify(emailService, times(1)).send(emailTemplateCaptor.capture());
            EmailTemplateAssert.that(emailTemplateCaptor.getValue())
                    .hasReceiverEmailAddress(receiverEmailAddress)
                    .hasReceiverName(receiverEmailAddress)
                    .hasSubject("ReadyBid: New Custom RFP Message from " + message.company)
                    .hasEmptyCC()
                    .hasHtmlTemplate(TemplateService.CONTACT_US_TEMPLATE)
                    .modelAssert(model -> model
                            .contains("FIRST_NAME", "Colleague")
                            .contains("MESSAGE", "New Custom RFP Message has been received with following details:")
                            .contains("SUBJECT", message.company)
                            .contains("FROM", message.lastName + " " + message.firstName)
                            .contains("EMAIL", message.emailAddress)
                            .contains("PHONE", message.phone)
                            .contains("CREATED_AT", DateTimeFormatter.RFC_1123_DATE_TIME.format(message.createdAt))
                            .contains("TEXT", message.message));
        }
    }
}