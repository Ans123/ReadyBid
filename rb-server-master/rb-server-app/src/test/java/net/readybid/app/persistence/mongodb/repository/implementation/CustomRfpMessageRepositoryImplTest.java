package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.persistence.mongodb.repository.CustomRfpMessageRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.CustomRfpMessageCollection;
import net.readybid.app.use_cases.contact_us.CustomRfpMessage;
import net.readybid.app.use_cases.contact_us.CustomRfpMessageFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@DisplayName("CustomRfpMessageRepositoryImpl ")
class CustomRfpMessageRepositoryImplTest {

    private CustomRfpMessageRepository sut;

    private MongoDatabase db;
    private MongoCollection<CustomRfpMessage> collection;

    @BeforeEach
    void setUp(){
        //noinspection unchecked
        collection = mock(MongoCollection.class);
        db = mock(MongoDatabase.class);
        doReturn(collection).when(db).getCollection(same(CustomRfpMessageCollection.COLLECTION_NAME), same(CustomRfpMessage.class));

        sut = new CustomRfpMessageRepositoryImpl(db);
    }

    @Nested
    @DisplayName("on insert ")
    class InsertTest {

        final Consumer<CustomRfpMessage> mut = customRfpMessage -> sut.insert(customRfpMessage);

        @Test
        @DisplayName("should insert to collection ")
        void insertTest() {
            final CustomRfpMessage message = CustomRfpMessageFactory.random();

            mut.accept(message);

            verify(collection, times(1)).insertOne(same(message));
        }
    }
}