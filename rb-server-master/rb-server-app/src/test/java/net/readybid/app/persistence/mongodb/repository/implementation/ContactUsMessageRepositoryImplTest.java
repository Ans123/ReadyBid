package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.persistence.mongodb.repository.ContactUsMessageRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.ContactUsMessageCollection;
import net.readybid.app.use_cases.contact_us.ContactUsMessage;
import net.readybid.app.use_cases.contact_us.ContactUsMessageFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@DisplayName("ContactUsMessageRepositoryImpl ")
class ContactUsMessageRepositoryImplTest {

    private ContactUsMessageRepository sut;

    private MongoCollection<ContactUsMessage> collection;

    @BeforeEach
    void setUp(){
        final MongoDatabase db = mock(MongoDatabase.class);
        //noinspection unchecked
        collection = mock(MongoCollection.class);

        doReturn(collection).when(db).getCollection(same(ContactUsMessageCollection.COLLECTION_NAME), same(ContactUsMessage.class));

        sut = new ContactUsMessageRepositoryImpl(db);
    }

    @Nested
    @DisplayName("on insert ")
    class InsertTest {

        final Consumer<ContactUsMessage> mut = contactUsMessage -> sut.insert(contactUsMessage);

        @Test
        @DisplayName("should insert to collection ")
        void insertTest() {
            final ContactUsMessage message = ContactUsMessageFactory.random();

            mut.accept(message);

            verify(collection, times(1)).insertOne(same(message));
        }
    }
}