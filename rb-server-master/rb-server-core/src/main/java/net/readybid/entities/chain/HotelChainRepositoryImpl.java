package net.readybid.entities.chain;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.entities.core.EntityStatus;
import net.readybid.mongodb.MongoRetry;
import net.readybid.utils.ListResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;
import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 1/19/2017.
 *
 */
@Service
public class HotelChainRepositoryImpl implements HotelChainRepository {

    private final MongoDatabase mongoDatabase;
    private final MongoCollection<HotelChainImpl> chainCollection;
    private final MongoCollection<HotelChainImpl> unvalidatedChainCollection;

    @Autowired
    public HotelChainRepositoryImpl(
            @Qualifier("mongoDatabaseMain") MongoDatabase mongoDatabase
    ) {
        this.mongoDatabase = mongoDatabase;
        chainCollection = mongoDatabase.getCollection("Chain", HotelChainImpl.class);
        unvalidatedChainCollection = mongoDatabase.getCollection("UnvalidatedChain", HotelChainImpl.class);
    }

    @Override
    @MongoRetry
    public ListResult<HotelChainListItemViewModel> listAllChains() {
        final MongoCollection<HotelChainListItemViewModel> collection = mongoDatabase.getCollection("Chain", HotelChainListItemViewModel.class);
        final List<HotelChainListItemViewModel> data = collection.find(byStatusValue(EntityStatus.ACTIVE))
                .sort(orderBy(ascending("name")))
                .into(new ArrayList<>());
        return new ListResult<>(data);
    }

    @Override
    @MongoRetry
    public HotelChain findById(String id) {
        return getChain(byId(id), null).first();
    }

    @Override
    @MongoRetry
    public void saveForValidation(HotelChain entity) {
        unvalidatedChainCollection.insertOne((HotelChainImpl) entity);
    }

    @Override
    public HotelChain findByIdIncludingUnverified(String id) {
        return findByIdIncludingUnverified(id, (String[]) null);
    }

    @Override
    @MongoRetry
    public HotelChainImpl findByIdIncludingUnverified(String id, String... fields) {
        final Document projection = fields == null ? null : include(fields);
        final HotelChainImpl hc = getChain(byId(id), projection).first();

        return hc == null ? getUnvalidatedChain(byId(id), projection).first() : hc;
    }

    private AggregateIterable<HotelChainImpl> getChain(Bson query, Bson projection){
        return chainCollection.aggregate(joinCreatedAndStatus(query, projection));
    }

    private AggregateIterable<HotelChainImpl> getUnvalidatedChain(Bson query, Bson projection){
        return unvalidatedChainCollection.aggregate(joinCreatedAndStatus(query, projection));
    }
}
