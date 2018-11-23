package net.readybid.api.main.rfp.destinations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.entities.traveldestination.TravelDestinationImpl;
import net.readybid.app.core.entities.traveldestination.TravelDestinationStatus;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.mongodb.MongoRetry;
import net.readybid.app.persistence.mongodb.repository.mapping.HotelRfpCollection;
import net.readybid.mongodb.collections.TravelDestinationCollection;
import net.readybid.rfphotel.destinations.web.TravelDestinationListItemViewModel;
import net.readybid.utils.ListResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static net.readybid.mongodb.RbMongoFilters.*;

/**
 * Created by DejanK on 4/4/2017.
 *
 */
@Service
public class TravelDestinationsRepositoryImpl implements TravelDestinationsRepository {

    private final MongoCollection<TravelDestinationListItemViewModel> listViewCollection;
    private final MongoCollection<TravelDestinationListItemViewModel> rfpListViewCollection;
    private final MongoCollection<TravelDestinationImpl> destinationsCollection;

    private final RfpAccessControlService rfpAccessControlService;
    private final MongoDatabase db;

    @Autowired
    public TravelDestinationsRepositoryImpl(MongoDatabase mongoDatabase, RfpAccessControlService rfpAccessControlService) {
        this.rfpAccessControlService = rfpAccessControlService;
        listViewCollection = mongoDatabase.getCollection(TravelDestinationCollection.COLLECTION_NAME,
                TravelDestinationListItemViewModel.class);
        rfpListViewCollection = mongoDatabase.getCollection(HotelRfpCollection.COLLECTION_NAME,
                TravelDestinationListItemViewModel.class);
        destinationsCollection = mongoDatabase.getCollection(TravelDestinationCollection.COLLECTION_NAME,
                TravelDestinationImpl.class);
        db = mongoDatabase;
    }

    @Override
    @MongoRetry
    public TravelDestination getById(String destinationId, String rfpId) {
        return destinationsCollection.aggregate(joinCreatedAndStatus(and(
                byId(destinationId),
                eq("rfpId", oid(rfpId)),
                skipStatus(TravelDestinationStatus.DELETED))
        )).first();
    }

    @Override
    @MongoRetry
    public ListResult<TravelDestinationListItemViewModel> listUserTravelDestinationsWithRfpName(AuthenticatedUser user) {
        final List<TravelDestinationListItemViewModel> list = rfpListViewCollection.aggregate(Arrays.asList(
                match(rfpAccessControlService.queryAsDocument()),
                project(new Document("rfpName", "$specifications.name")
                        .append("rfpId", "$_id")),
                lookup(TravelDestinationCollection.COLLECTION_NAME, "rfpId", "rfpId", "td"),
                unwind("$td", false),
                match(new Document("td.status.value", new Document("$not", new Document("$eq", "DELETED")))),
                project(new Document("td.rfpName", "$rfpName")
                        .append("td._id", 1)
                        .append("td.rfpId", 1)
                        .append("td.name", 1)),
                Aggregates.replaceRoot("$td"),
                sort(doc("name", 1))
        )).into(new ArrayList<>());

        return new ListResult<>(list);
    }

    @Override
    @MongoRetry
    public ListResult<TravelDestinationListItemViewModel> listRfpTravelDestinationsWithRfpName(String rfpId) {
        final List<TravelDestinationListItemViewModel> list = listViewCollection.aggregate(Arrays.asList(
                match(new Document("$and", Arrays.asList(
                        new Document("rfpId", oid(rfpId)),
                        new Document("status.value", new Document("$not", new Document("$eq", "DELETED")))
                ))),
                project(new Document("rfpId", 1)
                        .append("name", 1)),
                lookup(HotelRfpCollection.COLLECTION_NAME, "rfpId", "_id", "rfp"),
                unwind("$rfp", false),
                project(new Document("rfpId", 1)
                        .append("name", 1)
                        .append("rfpName", "$rfp.specifications.name")),
                sort(doc("name", 1))
        )).into(new ArrayList<>());

        return new ListResult<>(list);
    }
}
