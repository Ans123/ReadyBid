package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.mapping.TravelAgencyCollection;
import net.readybid.entities.agency.core.AgencyImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("NewTravelAgencyRepositoryImpl")
public class TravelAgencyRepositoryImpl implements ShadowEntityRepository {

    private final MongoCollection<AgencyImpl> collection;
    private final MongoCollection<AgencyImpl> unvalidatedCollection;

    @Autowired
    public TravelAgencyRepositoryImpl(MongoDatabase database, EntityRepositoryLibrary entityRepositoryLibrary) {
        entityRepositoryLibrary.register(EntityType.TRAVEL_AGENCY, this);
        this.collection = database.getCollection(TravelAgencyCollection.COLLECTION_NAME, AgencyImpl.class);
        unvalidatedCollection = database.getCollection(TravelAgencyCollection.UNVALIDATED_COLLECTION_NAME, AgencyImpl.class);
    }

    @Override
    @MongoRetry
    public AgencyImpl findOne(Bson filter, Bson projection, boolean includeUnvalidated) {
        final AgencyImpl agency = collection.find(filter).projection(projection).first();
        return agency == null && includeUnvalidated
                ? unvalidatedCollection.find(filter).projection(projection).first()
                : agency;
    }

    @Override
    @MongoRetry
    public AgencyImpl findOneAndUpdate(Bson query, Bson update, FindOneAndUpdateOptions options) {
        final AgencyImpl agency = collection.findOneAndUpdate(query, update, options);
        return agency == null
                ? unvalidatedCollection.findOneAndUpdate(query, update, options)
                : agency;
    }
}
