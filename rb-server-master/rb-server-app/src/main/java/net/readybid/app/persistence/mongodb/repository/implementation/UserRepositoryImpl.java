package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.persistence.mongodb.repository.UserRepository;
import net.readybid.auth.user.UserImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.Date;

import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.UserCollection.CHANGED;
import static net.readybid.app.persistence.mongodb.repository.mapping.UserCollection.COLLECTION_NAME;
import static net.readybid.app.persistence.mongodb.repository.mapping.UserCollection.PASSWORD;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final MongoCollection<UserImpl> userCollection;

    public UserRepositoryImpl(MongoDatabase database) {
        this.userCollection = database.getCollection(COLLECTION_NAME, UserImpl.class);
    }

    @Override
    @MongoRetry
    public UserImpl findAndUpdate(Bson query, Bson update, FindOneAndUpdateOptions options) {
        options.projection(exclude(PASSWORD));
        return userCollection.findOneAndUpdate(query, markChanged(update), options);
    }

    @Override
    @MongoRetry
    public void update(Bson query, Bson update) {
        userCollection.updateMany(query, markChanged(update));
    }

    private Bson markChanged(Bson update){
        return combine(update,set(CHANGED, new Date().getTime()));
    }
}