package net.readybid.app.use_cases.contact_us;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

@DisplayName("ContactUsMessage ")
class ContactUsMessageTest {

    @Nested
    @DisplayName("when created ")
    class CreateTest {

        @Test
        @DisplayName("Should contain values")
        void createContactUsMessageTest() {
            final String name = RbRandom.alphanumeric(50, false);
            final String emailAddress = RbRandom.emailAddress();
            final String message = RbRandom.alphanumeric(1000, false);
            final ZonedDateTime creationMark = ZonedDateTime.now();

            ContactUsMessageAssert.that(new ContactUsMessage(name, emailAddress, message))
                    .hasName(name)
                    .hasEmailAddress(emailAddress)
                    .hasMessage(message)
                    .hasCreatedAtAfter(creationMark);
        }
    }
}