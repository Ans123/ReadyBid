package net.readybid.app.interactors.rfp_hotel.bid_manager.view;

import com.mongodb.DuplicateKeyException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.bidmanagerview.BidManagerView;
import net.readybid.bidmanagerview.BidManagerViewImpl;
import net.readybid.bidmanagerview.BidManagerViewListItemViewModel;
import net.readybid.mongodb.MongoRetry;
import net.readybid.mongodb.collections.BidManagerViewCollection;
import net.readybid.utils.ListResult;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.mongodb.RbMongoFilters.byId;
import static net.readybid.mongodb.RbMongoFilters.oid;

/**
 * Created by DejanK on 4/3/2017.
 *
 */
@Service
public class BidManagerViewRepositoryImpl implements BidManagerViewRepository {

    private final MongoCollection<BidManagerViewImpl> bmViewCollection;

    @Autowired
    public BidManagerViewRepositoryImpl(MongoDatabase defaultMongoDatabase) {
        bmViewCollection = defaultMongoDatabase.getCollection(BidManagerViewCollection.COLLECTION_NAME, BidManagerViewImpl.class);
    }

    @Override
    @MongoRetry
    public void createView(BidManagerView view) {
        try {
            bmViewCollection.insertOne((BidManagerViewImpl) view);
        } catch (DuplicateKeyException ignore){}
    }

    @Override
    @MongoRetry
    public BidManagerView findViewByNameForUser(String viewName, AuthenticatedUser user) {
        return bmViewCollection.find(and(eq("name", viewName), byOwnerId(user))).first();    }

    @Override
    @MongoRetry
    public void updateViewName(String rfpId, String viewName) {
        bmViewCollection.updateMany(eq("rfpId", oid(rfpId)), set("name", viewName));
    }

    @Override
    @MongoRetry
    public BidManagerView findViewById(String viewId, AuthenticatedUser user) {
        return bmViewCollection.find(and(byId(viewId), byOwnerId(user))).first();
    }

    @Override
    public BidManagerView findViewByRfpIdAndOwner(String rfpId, AuthenticatedUser user) {
        return bmViewCollection.find(and(eq("rfpId", oid(rfpId)), byOwnerId(user))).first();
    }

    @Override
    @MongoRetry
    public ListResult<BidManagerViewListItemViewModel> listViews(AuthenticatedUser user) {
        final List<BidManagerViewImpl> views = bmViewCollection
                .find(byOwnerId(user))
                .projection(include("name", "rfpType", "type", "rfpId"))
                .sort(descending("_id"))
                .into(new ArrayList<>());

        return new ListResult<>(BidManagerViewListItemViewModel.FACTORY.createList(views), views.size());
    }

    private Bson byOwnerId(AuthenticatedUser user) {
        return eq("ownerId", user.getAccountId());
    }
}
