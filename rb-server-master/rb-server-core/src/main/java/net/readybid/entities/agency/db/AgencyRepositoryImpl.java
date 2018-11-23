package net.readybid.entities.agency.db;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.entities.agency.core.Agency;
import net.readybid.entities.agency.core.AgencyImpl;
import net.readybid.entities.agency.logic.AgencyRepository;
import net.readybid.mongodb.MongoRetry;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 1/6/2017.
 *
 */
@Service
public class AgencyRepositoryImpl implements AgencyRepository {

    private final MongoCollection<AgencyImpl> agencyCollection;
    private final MongoCollection<AgencyImpl> unvalidatedAgencyCollection;

    @Autowired
    public AgencyRepositoryImpl(
            @Qualifier("mongoDatabaseMain") MongoDatabase mongoDatabase
    ) {
        agencyCollection = mongoDatabase.getCollection("TravelAgency", AgencyImpl.class);
        unvalidatedAgencyCollection = mongoDatabase.getCollection("UnvalidatedTravelAgency", AgencyImpl.class);
    }

    @Override
    @MongoRetry
    public Agency findById(String id) {
        return getAgency(byId(id), null).first();
    }

    @Override
    @MongoRetry
    public void saveForValidation(Agency agency) {
        unvalidatedAgencyCollection.insertOne((AgencyImpl) agency);
    }

    @Override
    @MongoRetry
    public AgencyImpl findByIdIncludingUnverified(ObjectId id, String... fields) {
        final Document projection = fields == null ? null : include(fields);
        final AgencyImpl hc = getAgency(byId(id), projection).first();

        return hc == null ? getUnvalidatedAgency(byId(id), projection).first() : hc;
    }

    @Override
    public AgencyImpl findByIdIncludingUnverified(String id, String... fields) {
        return findByIdIncludingUnverified(oid(id), fields);
    }

    private AggregateIterable<AgencyImpl> getAgency(Bson query, Bson projection){
        return agencyCollection.aggregate(joinCreatedAndStatus(query, projection));
    }

    private AggregateIterable<AgencyImpl> getUnvalidatedAgency(Bson query, Bson projection){
        return unvalidatedAgencyCollection.aggregate(joinCreatedAndStatus(query, projection));
    }
}
