package net.readybid.app.use_cases.contact_us;

import net.readybid.test_utils.RbRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

@DisplayName("CustomRfpMessage ")
class CustomRfpMessageTest {

    @Nested
    @DisplayName("when created ")
    class CreateTest {

        @Test
        @DisplayName("Should have values")
        void createCustomRfpMessageTest() {
            final String firstName = RbRandom.alphanumeric(100, false);
            final String lastName = RbRandom.alphanumeric(100, false);
            final String emailAddress = RbRandom.alphanumeric(250, false);
            final String phone = RbRandom.alphanumeric(20, false);
            final String company = RbRandom.alphanumeric(100, false);
            final String message = RbRandom.alphanumeric(1000, false);
            final ZonedDateTime creationMark = ZonedDateTime.now();

            CustomRfpMessageAssert.that( new CustomRfpMessage(firstName, lastName, emailAddress, phone, company, message))
                    .hasFirstName(firstName)
                    .hasLastName(lastName)
                    .hasEmailAddress(emailAddress)
                    .hasPhone(phone)
                    .hasCompany(company)
                    .hasMessage(message)
                    .hasCreatedAtAfter(creationMark);
        }
    }
}