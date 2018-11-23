package net.readybid.auth.invitation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.mongodb.MongoRetry;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.lt;
import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.byStatusValue;
import static net.readybid.mongodb.RbMongoFilters.joinCreatedAndStatus;

/**
 * Created by DejanK on 5/18/2017.
 *
 */
@Service
public class InvitationRepositoryImpl implements InvitationRepository {

    private final MongoCollection<InvitationImpl> invitationCollection;

    public InvitationRepositoryImpl(MongoDatabase mainMongoDatabase) {
        this.invitationCollection = mainMongoDatabase.getCollection("Invitation", InvitationImpl.class);
    }

    @Override
    @MongoRetry
    public void create(Invitation invitation) {
        invitationCollection.insertOne((InvitationImpl) invitation);
    }

    @Override
    @MongoRetry
    public Invitation getActiveInvitationById(String id) {
        cleanup();
        return invitationCollection
                .aggregate(joinCreatedAndStatus(and(byId(id), byStatusValue(InvitationStatus.CREATED))))
                .first();
    }

    @Override
    @MongoRetry
    public void cleanup() {
        invitationCollection.deleteMany(lt("expiryDate", new Date().getTime()));
    }
}
