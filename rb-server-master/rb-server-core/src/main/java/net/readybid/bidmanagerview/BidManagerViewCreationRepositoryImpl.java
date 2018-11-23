package net.readybid.bidmanagerview;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.readybid.mongodb.MongoRetry;
import net.readybid.mongodb.collections.BidManagerViewCollection;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Created by DejanK on 5/19/2017.
 *
 */
@Service
public class BidManagerViewCreationRepositoryImpl implements BidManagerViewCreationRepository {

    private final MongoCollection<BidManagerViewImpl> bmViewCollection;

    @Autowired
    public BidManagerViewCreationRepositoryImpl(MongoDatabase defaultMongoDatabase) {
        bmViewCollection = defaultMongoDatabase.getCollection(BidManagerViewCollection.COLLECTION_NAME, BidManagerViewImpl.class);
    }

    @Override
    @MongoRetry
    public BidManagerView createViewIfDoesNotExist(BidManagerView view) {
        final List<Bson> filters = new ArrayList<>();
        filters.add(eq("ownerId", view.getOwner()));
        filters.add(eq("type", view.getType()));
        filters.add(eq("rfpType", view.getRfpType()));
        filters.add(eq("side", view.getSide()));
        if(view.getRfpId() != null) filters.add(eq("rfpId", view.getRfpId()));

        BidManagerView saved = bmViewCollection.find(and(filters)).first();
        if(saved == null){
            bmViewCollection.insertOne((BidManagerViewImpl) view);
            saved = view;
        }

        return saved;
    }

    @Override
    @MongoRetry
    public void createViews(List<BidManagerView> views) {
        if(!(views == null || views.isEmpty())) {
            final List<BidManagerViewImpl> viewImplList = views.stream()
                    .map(v -> (BidManagerViewImpl) v).collect(Collectors.toList());
            try {
                bmViewCollection.insertMany(viewImplList);
            } catch (MongoBulkWriteException ignore){ }
        }
    }
}
