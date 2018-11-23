package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.UpdateResult;
import net.readybid.app.persistence.mongodb.repository.HotelRfpRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection;
import net.readybid.mongodb.MongoRetry;
import net.readybid.rfp.core.RfpImpl;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HotelRfpRepositoryImpl implements HotelRfpRepository {

    private final MongoCollection<RfpImpl> collection;

    @Autowired
    public HotelRfpRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection(HotelRfpCollection.COLLECTION_NAME, RfpImpl.class);
    }

    @Override
    @MongoRetry
    public void bulkWrite(List<WriteModel<RfpImpl>> writes) {
        collection.bulkWrite(writes);
    }

    @Override
    public RfpImpl findOne(Bson filter, Bson project) {
        return collection.find(filter).projection(project).first();
    }

    @Override
    @MongoRetry
    public UpdateResult updateOne(Bson filter, Bson update) {
        return collection.updateOne(filter, update);
    }
}
