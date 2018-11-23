package net.readybid.app.gate.repository;

import com.mongodb.BulkWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.app.core.entities.traveldestination.*;
import net.readybid.app.core.service.traveldestination.LoadTravelDestinationRepository;
import net.readybid.app.core.service.traveldestination.SaveTravelDestinationRepository;
import net.readybid.exceptions.UnrecoverableException;
import net.readybid.mongodb.MongoRetry;
import net.readybid.mongodb.collections.TravelDestinationCollection;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.mongodb.RbMongoFilters.*;

@Service
public class TravelDestinationRepositoryImpl implements SaveTravelDestinationRepository, LoadTravelDestinationRepository {

    private final MongoCollection<TravelDestinationImpl> destinationCollection;

    @Autowired
    public TravelDestinationRepositoryImpl(MongoDatabase mongoDatabase) {
        destinationCollection = mongoDatabase.getCollection(TravelDestinationCollection.COLLECTION_NAME, TravelDestinationImpl.class);
    }

    @Override
    @MongoRetry
    public void create(TravelDestinationImpl destination) {
        destinationCollection.insertOne(destination);
    }

    @Override
    @MongoRetry
    public TravelDestinationImpl getById(String destinationId) {
        return destinationCollection.aggregate(
                joinCreatedAndStatus(and(byId(destinationId), skipStatus(TravelDestinationStatus.DELETED)))
        ).first();
    }

    @Override
    @MongoRetry
    public void save(TravelDestinationImpl destination) {
        destinationCollection.replaceOne(byId(destination.getId()), destination);
    }

    @Override
    @MongoRetry
    public void markAsDeleted(String destinationId, TravelDestinationStatusDetails deletedStatus) {
        destinationCollection.updateOne(
                and(byId(destinationId), skipStatus(TravelDestinationStatus.DELETED)),
                set("status", deletedStatus)
        );
    }

    @Override
    @MongoRetry
    public void setFilter(String destinationId, TravelDestinationHotelFilter filter) {
        destinationCollection.updateOne(byId(destinationId),set("filter", filter));

    }

    @Override
    @MongoRetry
    public void updateRfpTravelDestinationFilters(String rfpId, TravelDestinationHotelFilter filter){
        destinationCollection.updateMany(
                eq("rfpId", oid(rfpId)),
                set("filter", filter)
        );
    }

    @Override
    @MongoRetry
    public List<TravelDestinationImpl> listByRfpId(String rfpId) {
        final Bson filter = and(eq("rfpId", oid(rfpId)), skipStatus(TravelDestinationStatus.DELETED));
        return destinationCollection.aggregate(joinCreatedAndStatus(filter)).into(new ArrayList<>());
    }

    @Override
    @MongoRetry
    public void createAll(List<? extends TravelDestinationImpl> travelDestinations) {
        try {
            destinationCollection.insertMany(travelDestinations);
        } catch (BulkWriteException bwe){
            final List<ObjectId> tdsIds = travelDestinations.stream().map(td -> new ObjectId(td.getId())).collect(Collectors.toList());
            destinationCollection.deleteMany(in("_id", tdsIds));
            throw new UnrecoverableException(bwe);
        }
    }
}
