package net.readybid.api.contact_us;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.entities.email.EmailTemplate;
import net.readybid.app.entities.email.EmailTemplateAssert;
import net.readybid.app.interactors.email.EmailService;
import net.readybid.test_utils.RbMapAssert;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static net.readybid.app.persistence.mongodb.repository.mapping.ContactUsMessageCollection.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("[E2E] Receive Contact Us Message ")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-e2e-test.properties")
@ActiveProfiles("default,e2eTest")
class ReceiveContactUsMessageE2ETest {

    @Autowired private MongoDatabase mongoDatabase;

    @Autowired private EmailService emailService;

    @Autowired private TestRestTemplate restTemplate;

    @Value("${email.receiver.contactUs}") private String receiverEmail;

    private HttpHeaders headers;
    private MongoCollection<Document> contactUsMessageCollection;

    @BeforeEach
    void setup(){
        headers = new HttpHeaders();
        mongoDatabase.drop();
        contactUsMessageCollection = mongoDatabase.getCollection(COLLECTION_NAME);
    }

    @DisplayName("saves Message and sends Email")
    @Test
    void savesMessageInTheDatabaseTest() {
        final ZonedDateTime mark = ZonedDateTime.now();
        final ContactUsRequest request = ContactUsRequestFactory.random();
        final HttpEntity<ContactUsRequest> entity = new HttpEntity<>(request, headers);

        final ResponseEntity<String> response = restTemplate
                .exchange("/contact-us", HttpMethod.POST, entity, String.class);

        assertSame(HttpStatus.CREATED, response.getStatusCode());
        assertMessageIsSavedInDatabase(request, mark);
        assertEMailIsSentToContactUsPerson(request, mark);
    }

    private void assertMessageIsSavedInDatabase(ContactUsRequest request, ZonedDateTime mark) {
        final List<Document> savedMessages = contactUsMessageCollection.find().into(new ArrayList<>());
        assertEquals(1, savedMessages.size());
        RbMapAssert.that(savedMessages.get(0))
                .contains(NAME, request.name)
                .contains(EMAIL_ADDRESS, request.emailAddress)
                .contains(MESSAGE, request.message)
                .hasAfter(CREATED_AT, mark);
    }

    private void assertEMailIsSentToContactUsPerson(ContactUsRequest request, ZonedDateTime mark) {
        final ArgumentCaptor<EmailTemplate> captor = ArgumentCaptor.forClass(EmailTemplate.class);
        verify(emailService, times(1)).send(captor.capture());

        EmailTemplateAssert.that(captor.getValue())
                .hasSubject("ReadyBid: New Contact Us Message")
                .hasReceiverName(receiverEmail)
                .hasReceiverEmailAddress(receiverEmail)
                .hasEmptyCC()
                .hasHtmlTemplate("/templates/contact-us-email.html")
                .modelAssert(model -> model
                        .contains("FIRST_NAME", "Colleague")
                        .contains("MESSAGE", "New Contact Us Message has been received with following details:")
                        .contains("FROM", request.name)
                        .contains("EMAIL", request.emailAddress)
                        .contains("TEXT", request.message)
                        .hasAfter("CREATED_AT", mark, DateTimeFormatter.RFC_1123_DATE_TIME)
                );
    }
}