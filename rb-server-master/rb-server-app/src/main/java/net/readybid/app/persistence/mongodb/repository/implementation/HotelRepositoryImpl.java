package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.HotelRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelCollection;
import net.readybid.entities.hotel.core.HotelImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("HotelRepository")
public class HotelRepositoryImpl implements HotelRepository, ShadowEntityRepository {

    private final MongoCollection<HotelImpl> collection;
    private final MongoCollection<HotelImpl> unvalidatedCollection;

    @Autowired
    public HotelRepositoryImpl(MongoDatabase database, EntityRepositoryLibrary entityRepositoryLibrary) {
        entityRepositoryLibrary.register(EntityType.HOTEL, this);
        this.collection = database.getCollection(HotelCollection.COLLECTION_NAME, HotelImpl.class);
        unvalidatedCollection = database.getCollection(HotelCollection.UNVALIDATED_COLLECTION_NAME, HotelImpl.class);
    }

    @Override
    @MongoRetry
    public HotelImpl findOne(Bson filter, Bson projection, boolean includeUnvalidated) {
        final HotelImpl hotel = collection.find(filter).projection(projection).first();
        return hotel == null && includeUnvalidated
                ? unvalidatedCollection.find(filter).projection(projection).first()
                : hotel;
    }

    @Override
    @MongoRetry
    public HotelImpl findOneAndUpdate(Bson query, Bson update, FindOneAndUpdateOptions options) {
        final HotelImpl hotel = collection.findOneAndUpdate(query, update, options);
        return hotel == null
                ? unvalidatedCollection.findOneAndUpdate(query, update, options)
                : hotel;
    }

    @Override
    public void update(Bson filter, Bson update) {
        collection.updateMany(filter, update);
        unvalidatedCollection.updateMany(filter, update);
    }

    @Override
    public List<HotelImpl> find(Bson filter, Bson projection) {
        final List<HotelImpl> hotels = collection.find(filter).projection(projection).into(new ArrayList<>());
        final List<HotelImpl> unvalidatedHotels = unvalidatedCollection.find(filter).projection(projection).into(new ArrayList<>());
        hotels.addAll(unvalidatedHotels);
        return hotels;
    }
}
