package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import net.readybid.app.persistence.mongodb.repository.HotelRfpBidQueryViewRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HotelRfpBidQueryViewRepositoryImpl implements HotelRfpBidQueryViewRepository {

    private final MongoCollection<HotelRfpBidQueryView.Builder> collection;

    @Autowired
    public HotelRfpBidQueryViewRepositoryImpl(MongoDatabase mongoDatabase) {
        this.collection = mongoDatabase.getCollection(HotelRfpBidCollection.COLLECTION_NAME, HotelRfpBidQueryView.Builder.class);
    }

    @Override
    public List<HotelRfpBidQueryView.Builder> findAll(Bson filter, Bson projection) {
        return collection.find(filter).projection(projection).into(new ArrayList<>());
    }
}
