package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.interactors.contact_us.gate.CustomRfpMessageStorageManager;
import net.readybid.app.persistence.mongodb.repository.CustomRfpMessageRepository;
import net.readybid.app.use_cases.contact_us.CustomRfpMessage;
import net.readybid.mongodb.MongoRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static net.readybid.app.persistence.mongodb.repository.mapping.CustomRfpMessageCollection.COLLECTION_NAME;

@Repository
public class CustomRfpMessageRepositoryImpl implements CustomRfpMessageRepository, CustomRfpMessageStorageManager {

    private final MongoCollection<CustomRfpMessage> collection;

    @Autowired
    public CustomRfpMessageRepositoryImpl(MongoDatabase db) {
        collection = db.getCollection(COLLECTION_NAME, CustomRfpMessage.class);
    }

    @Override
    @MongoRetry
    public void insert(CustomRfpMessage customRfpMessage) {
        collection.insertOne(customRfpMessage);
    }
}