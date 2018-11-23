package net.readybid.api.main.entity.rateloadinginformation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.entities.core.EntityRepository;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.entities.rfp.EntityRateLoadingInformation;
import net.readybid.entities.rfp.EntityRateLoadingInformationImpl;
import net.readybid.entities.rfp.RateLoadingInformation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.mongodb.RbMongoFilters.*;

@Service
public class RateLoadingInformationRepositoryImpl implements RateLoadingInformationRepository {

    private final MongoCollection<EntityRateLoadingInformationImpl> rateLoadingCollection;
    private final EntityRepository entityRepository;

    @Autowired
    public RateLoadingInformationRepositoryImpl(MongoDatabase mongoDatabase, EntityRepository entityRepository) {
        rateLoadingCollection = mongoDatabase.getCollection(COLLECTION, EntityRateLoadingInformationImpl.class);
        this.entityRepository = entityRepository;
    }

    @Override
    public EntityRateLoadingInformation getByEntityId(ObjectId entityId) {
        EntityRateLoadingInformationImpl information = rateLoadingCollection.aggregate(Arrays.asList(
                match( doc("entityId", entityId) )
                // todo: should lookup for entity but cannot with entities split into multiple collections
        )).first();

        if(null == information){
            information = new EntityRateLoadingInformationImpl();
        }

        final Entity entity = entityRepository.findByIdIncludingUnverified(entityId);
        information.setEntity(entity);

        return information;
    }

    @Override
    public EntityRateLoadingInformation getByEntityId(String entityId) {
        return getByEntityId(oid(entityId));
    }

    @Override
    public void update(String entityId, EntityType type, List<RateLoadingInformation> information) {
        rateLoadingCollection.updateOne(
                eq("entityId", oid(entityId)),
                combine(
                        set("entityId", oid(entityId)),
                        set("entityType", type),
                        set("information", information)
                ),
                new UpdateOptions().upsert(true)
        );
    }
}
