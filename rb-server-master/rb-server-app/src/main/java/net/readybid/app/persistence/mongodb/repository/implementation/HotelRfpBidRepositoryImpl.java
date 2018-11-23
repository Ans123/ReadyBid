package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.UpdateResult;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidImpl;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HotelRfpBidRepositoryImpl implements HotelRfpBidRepository {

    private final MongoCollection<HotelRfpBidImpl> collection;

    HotelRfpBidRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection(HotelRfpBidCollection.COLLECTION_NAME, HotelRfpBidImpl.class);
    }

    @Override
    @MongoRetry
    public List<HotelRfpBidImpl> find(Bson filter, Bson projection) {
        return collection.find(filter).projection(projection).into(new ArrayList<>());
    }

    @Override
    @MongoRetry
    public HotelRfpBidImpl findOne(Bson filter, Bson projection) {
        return collection.find(filter).projection(projection).first();
    }

    @MongoRetry
    @Override
    public List<HotelRfpBidImpl> aggregate(List<Bson> pipeline) {
        return collection.aggregate(pipeline).into(new ArrayList<>());
    }

    @Override
    @MongoRetry
    public UpdateResult update(Bson filter, Bson update) {
        return collection.updateMany(filter, update);
    }

    @Override
    @MongoRetry
    public void bulkWrite(List<WriteModel<HotelRfpBidImpl>> writes, BulkWriteOptions options) {
        if(writes != null && !writes.isEmpty())
            collection.bulkWrite(writes, options);
    }
}
