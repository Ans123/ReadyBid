package net.readybid.entities.consultancy.db;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.entities.consultancy.core.Consultancy;
import net.readybid.entities.consultancy.core.ConsultancyImpl;
import net.readybid.entities.consultancy.logic.ConsultancyRepository;
import net.readybid.mongodb.MongoRetry;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
@Service
public class ConsultancyRepositoryImpl implements ConsultancyRepository {

    private final MongoCollection<ConsultancyImpl> consultancyCollection;
    private final MongoCollection<ConsultancyImpl> unvalidatedConsultancyCollection;

    @Autowired
    public ConsultancyRepositoryImpl(
            @Qualifier("mongoDatabaseMain") MongoDatabase mongoDatabase
    ) {
        consultancyCollection = mongoDatabase.getCollection("TravelConsultancy", ConsultancyImpl.class);
        unvalidatedConsultancyCollection = mongoDatabase.getCollection("UnvalidatedTravelConsultancy", ConsultancyImpl.class);
    }

    @Override
    @MongoRetry
    public Consultancy findById(String id) {
        return getConsultancy(byId(id), null).first();
    }

    @Override
    @MongoRetry
    public void saveForValidation(Consultancy consultancy) {
        unvalidatedConsultancyCollection.insertOne((ConsultancyImpl) consultancy);
    }

    @Override
    @MongoRetry
    public ConsultancyImpl findByIdIncludingUnverified(ObjectId id, String... fields) {
        final Document projection = fields == null ? null : include(fields);
        final ConsultancyImpl hc = getConsultancy(byId(id), projection).first();

        return hc == null ? getUnvalidatedConsultancy(byId(id), projection).first() : hc;
    }

    @Override
    public Entity findByIdIncludingUnverified(String id, String... fields) {
        return findByIdIncludingUnverified(oid(id), fields);
    }

    private AggregateIterable<ConsultancyImpl> getConsultancy(Bson query, Bson projection){
        return consultancyCollection.aggregate(joinCreatedAndStatus(query, projection));
    }

    private AggregateIterable<ConsultancyImpl> getUnvalidatedConsultancy(Bson query, Bson projection){
        return unvalidatedConsultancyCollection.aggregate(joinCreatedAndStatus(query, projection));
    }
}
