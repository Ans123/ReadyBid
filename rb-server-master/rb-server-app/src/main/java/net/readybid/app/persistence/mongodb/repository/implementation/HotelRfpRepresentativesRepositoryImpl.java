package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoDatabase;
import net.readybid.app.entities.rfp_hotel.HotelRfpRepresentative;
import net.readybid.app.persistence.mongodb.repository.HotelRfpRepresentativesRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.AccountCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpBidCollection;
import net.readybid.mongodb.MongoRetry;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HotelRfpRepresentativesRepositoryImpl implements HotelRfpRepresentativesRepository {

    private MongoDatabase mongoDatabase;

    @Autowired
    public HotelRfpRepresentativesRepositoryImpl(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    @MongoRetry
    public List<HotelRfpRepresentative> aggregateAccount(List<Bson> pipeline) {
        return mongoDatabase
                .getCollection(AccountCollection.COLLECTION_NAME, HotelRfpRepresentative.class)
                .aggregate(pipeline).into(new ArrayList<>());
    }

    @Override
    public List<HotelRfpRepresentative> aggregateBid(List<Bson> pipeline) {
        return mongoDatabase
                .getCollection(HotelRfpBidCollection.COLLECTION_NAME, HotelRfpRepresentative.class)
                .aggregate(pipeline).into(new ArrayList<>());
    }

    @Override
    public Document findInAccount(Bson query, Bson projection) {
        return mongoDatabase
                .getCollection(AccountCollection.COLLECTION_NAME)
                .find(query)
                .projection(projection)
                .first();
    }

    @Override
    public List<Document> aggregateHotel(List<Bson> pipeline) {
        return mongoDatabase
                .getCollection(HotelCollection.COLLECTION_NAME)
                .aggregate(pipeline).into(new ArrayList<>());
    }
}
