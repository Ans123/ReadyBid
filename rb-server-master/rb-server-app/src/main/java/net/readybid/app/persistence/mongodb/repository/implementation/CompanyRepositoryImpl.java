package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.mapping.CompanyCollection;
import net.readybid.entities.company.core.CompanyImpl;
import net.readybid.mongodb.MongoRetry;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("NewCompanyRepositoryImpl")
public class CompanyRepositoryImpl implements ShadowEntityRepository {

    private final MongoCollection<CompanyImpl> collection;
    private final MongoCollection<CompanyImpl> unvalidatedCollection;

    @Autowired
    public CompanyRepositoryImpl(MongoDatabase database, EntityRepositoryLibrary entityRepositoryLibrary) {
        entityRepositoryLibrary.register(EntityType.COMPANY, this);
        this.collection = database.getCollection(CompanyCollection.COLLECTION_NAME, CompanyImpl.class);
        unvalidatedCollection = database.getCollection(CompanyCollection.UNVALIDATED_COLLECTION_NAME, CompanyImpl.class);
    }

    @Override
    @MongoRetry
    public CompanyImpl findOne(Bson filter, Bson projection, boolean includeUnvalidated) {
        final CompanyImpl company = collection.find(filter).projection(projection).first();
        return company == null && includeUnvalidated
                ? unvalidatedCollection.find(filter).projection(projection).first()
                : company;
    }

    @Override
    @MongoRetry
    public CompanyImpl findOneAndUpdate(Bson query, Bson update, FindOneAndUpdateOptions options) {
        final CompanyImpl company = collection.findOneAndUpdate(query, update, options);
        return company == null
                ? unvalidatedCollection.findOneAndUpdate(query, update, options)
                : company;
    }
}
