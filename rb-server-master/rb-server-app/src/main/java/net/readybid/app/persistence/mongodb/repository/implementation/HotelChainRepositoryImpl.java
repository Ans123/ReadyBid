package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.HotelChainRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelChainCollection;
import net.readybid.entities.chain.HotelChain;
import net.readybid.entities.chain.HotelChainImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("NewHotelChainRepositoryImpl")
public class HotelChainRepositoryImpl implements HotelChainRepository, ShadowEntityRepository {

    private final MongoCollection<HotelChainImpl> collection;
    private final MongoCollection<HotelChainImpl> unvalidatedCollection;

    @Autowired
    public HotelChainRepositoryImpl(MongoDatabase database, EntityRepositoryLibrary entityRepositoryLibrary) {
        entityRepositoryLibrary.register(EntityType.CHAIN, this);
        this.collection = database.getCollection(HotelChainCollection.COLLECTION_NAME, HotelChainImpl.class);
        unvalidatedCollection = database.getCollection(HotelChainCollection.UNVALIDATED_COLLECTION_NAME, HotelChainImpl.class);
    }

    @Override
    @MongoRetry
    public HotelChainImpl findOne(Bson filter, Bson projection, boolean includeUnvalidated) {
        final HotelChainImpl chain = collection.find(filter).projection(projection).first();
        return chain == null && includeUnvalidated
                ? unvalidatedCollection.find(filter).projection(projection).first()
                : chain;
    }

    @Override
    public HotelChain findOne(Bson filter, boolean includeUnvalidated) {
        return findOne(filter, null, includeUnvalidated);
    }

    @Override
    @MongoRetry
    public HotelChainImpl findOneAndUpdate(Bson query, Bson update, FindOneAndUpdateOptions options) {
        final HotelChainImpl chain = collection.findOneAndUpdate(query, update, options);
        return chain == null
                ? unvalidatedCollection.findOneAndUpdate(query, update, options)
                : chain;
    }

    @Override
    public void update(Bson filter, Bson update) {
        collection.updateMany(filter, update);
        unvalidatedCollection.updateMany(filter, update);
    }
}
