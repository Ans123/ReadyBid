package net.readybid.entities.hmc;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.mongodb.MongoRetry;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
@Service
public class HotelManagementCompanyRepositoryImpl implements HotelManagementCompanyRepository {

    private final MongoCollection<HotelManagementCompanyImpl> companyCollection;
    private final MongoCollection<HotelManagementCompanyImpl> unvalidatedCompanyCollection;

    @Autowired
    public HotelManagementCompanyRepositoryImpl(
            @Qualifier("mongoDatabaseMain") MongoDatabase mongoDatabase
    ) {
        companyCollection = mongoDatabase.getCollection(
                "HotelManagementCompany",
                HotelManagementCompanyImpl.class);

        unvalidatedCompanyCollection = mongoDatabase.getCollection(
                "UnvalidatedHotelManagementCompany",
                HotelManagementCompanyImpl.class);
    }

    @Override
    @MongoRetry
    public HotelManagementCompany findById(String id) {
        return getHotelManagementCompany(byId(id), null).first();
    }

    @Override
    @MongoRetry
    public void saveForValidation(HotelManagementCompany company) {
        unvalidatedCompanyCollection.insertOne((HotelManagementCompanyImpl) company);
    }

    @Override
    public HotelManagementCompany findByIdIncludingUnverified(String id) {
        return findByIdIncludingUnverified(id, (String[]) null);
    }

    @Override
    @MongoRetry
    public HotelManagementCompanyImpl findByIdIncludingUnverified(String id, String... fields) {
        final Document projection = fields == null ? null : include(fields);
        final HotelManagementCompanyImpl hc = getHotelManagementCompany(byId(id), projection).first();

        return hc == null ? getUnvalidatedHotelManagementCompany(byId(id), projection).first() : hc;
    }

    private AggregateIterable<HotelManagementCompanyImpl> getHotelManagementCompany(Bson query, Bson projection){
        return companyCollection.aggregate(joinCreatedAndStatus(query, projection));
    }

    private AggregateIterable<HotelManagementCompanyImpl> getUnvalidatedHotelManagementCompany(Bson query, Bson projection){
        return unvalidatedCompanyCollection.aggregate(joinCreatedAndStatus(query, projection));
    }
}
