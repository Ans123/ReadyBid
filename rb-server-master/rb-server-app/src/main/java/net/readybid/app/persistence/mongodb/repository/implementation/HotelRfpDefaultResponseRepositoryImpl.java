package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.WriteModel;
import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;
import net.readybid.app.persistence.mongodb.repository.HotelRfpDefaultResponseRepository;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static net.readybid.app.persistence.mongodb.repository.mapping.RfpDefaultResponseCollection.COLLECTION_NAME;

@Repository
public class HotelRfpDefaultResponseRepositoryImpl implements HotelRfpDefaultResponseRepository {

    private final MongoCollection<HotelRfpDefaultResponse> responseCollection;

    @Autowired
    public HotelRfpDefaultResponseRepositoryImpl(MongoDatabase db) {
        this.responseCollection = db.getCollection(COLLECTION_NAME, HotelRfpDefaultResponse.class);
    }

    @Override
    @MongoRetry
    public void update(Bson filter, Bson update) {
        responseCollection.updateMany(filter, update);
    }

    @Override
    public List<HotelRfpDefaultResponse> find(Bson filter, Bson projection) {
        return responseCollection.find(filter).projection(projection).into(new ArrayList<>());
    }

    @Override
    public void bulkWrite(List<WriteModel<HotelRfpDefaultResponse>> writes, BulkWriteOptions options) {
        responseCollection.bulkWrite(writes, options);
    }

    @Override
    public HotelRfpDefaultResponse findOne(Bson filter) {
        return responseCollection.find(filter).first();
    }
}
