package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.app.use_cases.contact_us.CustomRfpMessage;
import net.readybid.app.use_cases.contact_us.CustomRfpMessageFactory;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static net.readybid.app.persistence.mongodb.repository.mapping.CustomRfpMessageCollection.*;
import static org.mockito.Mockito.mock;

@DisplayName("CustomRfpMessageCollection ")
class CustomRfpMessageCollectionTest {

    @Nested
    @DisplayName("Codec ")
    class CodecTest {

        private RbMongoCodecWithProvider<CustomRfpMessage> codec;
        private CustomRfpMessage message;

        @BeforeEach
        void setUp(){
            codec = CustomRfpMessageCollection.codec(mock(BsonTypeClassMap.class));
            message = CustomRfpMessageFactory.random();
        }

        @Test
        @DisplayName("maps CustomRfpMessage")
        void shouldMapTest() {
            RbMongoCodecWithProviderAssert.that(codec)
                    .cannotRead()
                    .encodesToDocument(message, rbMapAssert -> rbMapAssert
                            .contains(FIRST_NAME, message.firstName)
                            .contains(LAST_NAME, message.lastName)
                            .contains(EMAIL_ADDRESS, message.emailAddress)
                            .contains(PHONE, message.phone)
                            .contains(COMPANY, message.company)
                            .contains(MESSAGE, message.message)
                            .contains(CREATED_AT, message.createdAt));
        }
    }
}