package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.persistence.mongodb.repository.InvitationRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.InvitationCollection;
import net.readybid.auth.invitation.InvitationImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

@Repository("NewInvitationRepositoryImpl")
public class InvitationRepositoryImpl implements InvitationRepository {

    private final MongoCollection<InvitationImpl> collection;

    public InvitationRepositoryImpl(MongoDatabase db) {
        this.collection = db.getCollection(InvitationCollection.COLLECTION_NAME, InvitationImpl.class);
    }

    @Override
    @MongoRetry
    public void update(Bson filter, Bson update) {
        collection.updateMany(filter, update);
    }
}
