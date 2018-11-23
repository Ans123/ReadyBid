package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.app.use_cases.contact_us.ContactUsMessage;
import net.readybid.app.use_cases.contact_us.ContactUsMessageFactory;
import net.readybid.mongodb.RbMongoCodecWithProvider;
import org.bson.codecs.BsonTypeClassMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static net.readybid.app.persistence.mongodb.repository.mapping.ContactUsMessageCollection.*;
import static org.mockito.Mockito.mock;

@DisplayName("ContactUsMessageCollection ")
class ContactUsMessageCollectionTest {

    @Nested
    @DisplayName("Codec ")
    class CodecTest {

        private RbMongoCodecWithProvider<ContactUsMessage> codec;
        private ContactUsMessage message;

        @BeforeEach
        void setUp(){
            codec = ContactUsMessageCollection.codec(mock(BsonTypeClassMap.class));
            message = ContactUsMessageFactory.random();
        }

        @Test
        @DisplayName("maps ContactUsMessage")
        void shouldMapTest() {
            RbMongoCodecWithProviderAssert.that(codec)
                    .cannotRead()
                    .encodesToDocument(message, rbMapAssert -> rbMapAssert
                            .contains(NAME, message.name)
                            .contains(EMAIL_ADDRESS, message.emailAddress)
                            .contains(MESSAGE, message.message)
                            .contains(CREATED_AT, message.createdAt));
        }
    }
}