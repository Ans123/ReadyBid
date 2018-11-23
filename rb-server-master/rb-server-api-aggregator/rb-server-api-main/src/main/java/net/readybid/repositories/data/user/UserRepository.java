package net.readybid.repositories.data.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.persistence.mongodb.repository.mapping.UserCollection;
import net.readybid.auth.login.GetActiveUserPasswordRepository;
import net.readybid.auth.user.UserStatus;
import net.readybid.mongodb.MongoRetry;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static net.readybid.mongodb.RbMongoFilters.byStatusValue;
import static net.readybid.mongodb.RbMongoFilters.include;

@Service
public class UserRepository implements GetActiveUserPasswordRepository {

    private final MongoCollection<Document> userCollection;

    @Autowired
    public UserRepository(MongoDatabase mongoDatabase) {
        this.userCollection = mongoDatabase.getCollection(UserCollection.COLLECTION_NAME);
    }

    @Override
    @MongoRetry
    public String getActiveUserPasswordByEmailAddress(String emailAddress) {
        final Document d = userCollection
                .find(and(eq(UserCollection.EMAIL_ADDRESS, emailAddress), byStatusValue(UserStatus.ACTIVE)))
                .projection(include(UserCollection.PASSWORD))
                .first();
        return d == null || !d.containsKey(UserCollection.PASSWORD) ? null : d.getString(UserCollection.PASSWORD);
    }
}
