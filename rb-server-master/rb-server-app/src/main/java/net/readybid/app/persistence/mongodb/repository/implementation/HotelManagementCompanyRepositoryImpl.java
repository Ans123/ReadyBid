package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelManagementCompanyCollection;
import net.readybid.entities.hmc.HotelManagementCompanyImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("NewHotelManagementCompanyRepositoryImpl")
public class HotelManagementCompanyRepositoryImpl implements ShadowEntityRepository {

    private final MongoCollection<HotelManagementCompanyImpl> collection;
    private final MongoCollection<HotelManagementCompanyImpl> unvalidatedCollection;

    @Autowired
    public HotelManagementCompanyRepositoryImpl(MongoDatabase database, EntityRepositoryLibrary entityRepositoryLibrary) {
        entityRepositoryLibrary.register(EntityType.HMC, this);
        this.collection = database.getCollection(HotelManagementCompanyCollection.COLLECTION_NAME, HotelManagementCompanyImpl.class);
        unvalidatedCollection = database.getCollection(HotelManagementCompanyCollection.UNVALIDATED_COLLECTION_NAME, HotelManagementCompanyImpl.class);
    }

    @Override
    @MongoRetry
    public HotelManagementCompanyImpl findOne(Bson filter, Bson projection, boolean includeUnvalidated) {
        final HotelManagementCompanyImpl hmc = collection.find(filter).projection(projection).first();
        return hmc == null && includeUnvalidated
                ? unvalidatedCollection.find(filter).projection(projection).first()
                : hmc;
    }

    @Override
    @MongoRetry
    public HotelManagementCompanyImpl findOneAndUpdate(Bson query, Bson update, FindOneAndUpdateOptions options) {
        final HotelManagementCompanyImpl hmc = collection.findOneAndUpdate(query, update, options);
        return hmc == null
                ? unvalidatedCollection.findOneAndUpdate(query, update, options)
                : hmc;
    }

}
