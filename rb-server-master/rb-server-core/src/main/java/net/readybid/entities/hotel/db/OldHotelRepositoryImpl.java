package net.readybid.entities.hotel.db;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.entities.hotel.core.HotelImpl;
import net.readybid.entities.hotel.logic.HotelRepository;
import net.readybid.mongodb.MongoRetry;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
@Service
public class OldHotelRepositoryImpl implements HotelRepository {

    private final MongoCollection<HotelImpl> hotelCollection;
    private final MongoCollection<HotelImpl> unvalidatedHotelCollection;

    @Autowired
    public OldHotelRepositoryImpl(
            @Qualifier("mongoDatabaseMain") MongoDatabase defaultMongoDatabase
    ) {
        hotelCollection = defaultMongoDatabase.getCollection("Hotel", HotelImpl.class);
        unvalidatedHotelCollection = defaultMongoDatabase.getCollection("UnvalidatedHotel", HotelImpl.class);
    }

    @Override
    @MongoRetry
    public Hotel findById(String id) {
        return getHotel(byId(id), null).first();
    }

    @Override
    public HotelImpl findByIdIncludingUnverified(String id) {
        return findByIdIncludingUnverified(id, (String[]) null);
    }

    @Override
    @MongoRetry
    public void saveForValidation(Hotel hotel) {
        unvalidatedHotelCollection.insertOne((HotelImpl) hotel);
    }

    @Override
    @MongoRetry
    public HotelImpl findByIdIncludingUnverified(ObjectId id, String... fields) {
        final Document projection = fields == null ? null : include(fields);
        final HotelImpl hc = getHotel(byId(id), projection).first();

        return hc == null ? getUnvalidatedHotel(byId(id), projection).first() : hc;
    }

    @Override
    public HotelImpl findByIdIncludingUnverified(String id, String... fields) {
        return findByIdIncludingUnverified(oid(id), fields);
    }

    private AggregateIterable<HotelImpl> getHotel(Bson query, Bson projection){
        return hotelCollection.aggregate(joinCreatedAndStatus(query, projection));
    }

    private AggregateIterable<HotelImpl> getUnvalidatedHotel(Bson query, Bson projection){
        return unvalidatedHotelCollection.aggregate(joinCreatedAndStatus(query, projection));
    }
}
