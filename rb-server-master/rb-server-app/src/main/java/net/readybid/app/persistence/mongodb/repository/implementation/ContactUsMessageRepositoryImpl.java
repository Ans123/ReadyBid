package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.interactors.contact_us.gate.ContactUsMessageStorageManager;
import net.readybid.app.persistence.mongodb.repository.ContactUsMessageRepository;
import net.readybid.app.use_cases.contact_us.ContactUsMessage;
import net.readybid.mongodb.MongoRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static net.readybid.app.persistence.mongodb.repository.mapping.ContactUsMessageCollection.COLLECTION_NAME;

@Repository
public class ContactUsMessageRepositoryImpl implements ContactUsMessageRepository, ContactUsMessageStorageManager {

    private final MongoCollection<ContactUsMessage> collection;

    @Autowired
    public ContactUsMessageRepositoryImpl(MongoDatabase db) {
        collection = db.getCollection(COLLECTION_NAME, ContactUsMessage.class);
    }

    @Override
    @MongoRetry
    public void insert(ContactUsMessage contactUsMessage) {
        collection.insertOne(contactUsMessage);
    }
}
