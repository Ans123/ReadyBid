package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.mapping.TravelConsultancyCollection;
import net.readybid.entities.consultancy.core.ConsultancyImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("NewTravelConsultancyRepositoryImpl")
public class TravelConsultancyRepositoryImpl implements ShadowEntityRepository {

    private final MongoCollection<ConsultancyImpl> collection;
    private final MongoCollection<ConsultancyImpl> unvalidatedCollection;

    @Autowired
    public TravelConsultancyRepositoryImpl(MongoDatabase database, EntityRepositoryLibrary entityRepositoryLibrary) {
        entityRepositoryLibrary.register(EntityType.TRAVEL_CONSULTANCY, this);
        this.collection = database.getCollection(TravelConsultancyCollection.COLLECTION_NAME, ConsultancyImpl.class);
        unvalidatedCollection = database.getCollection(TravelConsultancyCollection.UNVALIDATED_COLLECTION_NAME, ConsultancyImpl.class);
    }

    @Override
    public ConsultancyImpl findOne(Bson filter, Bson projection, boolean includeUnvalidated) {
        final ConsultancyImpl consultancy = collection.find(filter).projection(projection).first();
        return consultancy == null && includeUnvalidated
                ? unvalidatedCollection.find(filter).projection(projection).first()
                : consultancy;
    }

    @Override
    @MongoRetry
    public ConsultancyImpl findOneAndUpdate(Bson query, Bson update, FindOneAndUpdateOptions options) {
        final ConsultancyImpl consultancy = collection.findOneAndUpdate(query, update, options);
        return consultancy == null
                ? unvalidatedCollection.findOneAndUpdate(query, update, options)
                : consultancy;
    }
}
