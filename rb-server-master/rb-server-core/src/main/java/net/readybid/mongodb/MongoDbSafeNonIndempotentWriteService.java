package net.readybid.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.addToSet;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.pull;

@Service
public class MongoDbSafeNonIndempotentWriteService {

    private static final String PENDING_UPDATE_FIELD = "_pendingUpdate";

    public UpdateResult updateOne(MongoCollection collection, Bson match, List<Bson> updates) {
        final ObjectId updateId = prepareUpdate(collection, match);
        return updateOne(collection, updateId, match, updates);
    }

    @MongoRetry
    private UpdateResult updateOne(MongoCollection collection, ObjectId updateId, Bson match, List<Bson> updates) {
        updates.add(pull(PENDING_UPDATE_FIELD, updateId));
        return collection.updateOne(
                and(match, eq(PENDING_UPDATE_FIELD, updateId)),
                combine(updates));
    }

    @MongoRetry
    private ObjectId prepareUpdate(MongoCollection collection, Bson match){
        final ObjectId updateId = new ObjectId();
        collection.updateOne(match, addToSet(PENDING_UPDATE_FIELD, updateId));
        return updateId;
    }
}
