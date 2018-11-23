package net.readybid.entities.company.db;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.entities.company.core.Company;
import net.readybid.entities.company.core.CompanyImpl;
import net.readybid.entities.company.logic.CompanyRepository;
import net.readybid.mongodb.MongoRetry;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
@Service
public class CompanyRepositoryImpl implements CompanyRepository {

    private final MongoCollection<CompanyImpl> companyCollection;
    private final MongoCollection<CompanyImpl> unvalidatedCompanyCollection;

    @Autowired
    public CompanyRepositoryImpl(
            @Qualifier("mongoDatabaseMain") MongoDatabase mongoDatabase
    ) {
        companyCollection = mongoDatabase.getCollection("Company", CompanyImpl.class);
        unvalidatedCompanyCollection = mongoDatabase.getCollection("UnvalidatedCompany", CompanyImpl.class);
    }

    @Override
    @MongoRetry
    public Company findById(String id) {
        return getCompany(byId(id), null).first();
    }

    @Override
    @MongoRetry
    public void saveForValidation(Company company) {
        unvalidatedCompanyCollection.insertOne((CompanyImpl) company);
    }

    @Override
    @MongoRetry
    public Company findByIdIncludingUnverified(ObjectId id, String... fields) {
        final Document projection = fields == null ? null : include(fields);
        final Company c = getCompany(byId(id), projection).first();

        return c == null ? getUnvalidatedCompany(byId(id), projection).first() : c;
    }

    @Override
    public Entity findByIdIncludingUnverified(String id, String... fields) {
        return findByIdIncludingUnverified(oid(id), fields);
    }

    private AggregateIterable<CompanyImpl> getCompany(Bson query, Bson projection){
        return companyCollection.aggregate(joinCreatedAndStatus(query, projection));
    }

    private AggregateIterable<CompanyImpl> getUnvalidatedCompany(Bson query, Bson projection){
        return unvalidatedCompanyCollection.aggregate(joinCreatedAndStatus(query, projection));
    }
}
