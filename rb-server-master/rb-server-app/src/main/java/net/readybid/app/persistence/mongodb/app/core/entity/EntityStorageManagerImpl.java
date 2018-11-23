package net.readybid.app.persistence.mongodb.app.core.entity;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.gate.EntityStorageManager;
import net.readybid.app.persistence.mongodb.repository.EntityRepository;
import net.readybid.app.use_cases.core.entity.EntityBasicInformationUpdateRequest;
import net.readybid.elasticsearch.ElasticSearchOperations;
import net.readybid.entities.core.EntityMapper;
import net.readybid.entities.hotel.logic.ElasticSearchModificationSpecification;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Updates.*;
import static net.readybid.app.persistence.mongodb.repository.mapping.EntityCollection.*;
import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
class EntityStorageManagerImpl implements EntityStorageManager {

    private final EntityRepository repository;
    private final ElasticSearchOperations esOperations;

    @Autowired
    EntityStorageManagerImpl(EntityRepository repository, ElasticSearchOperations esOperations) {
        this.repository = repository;
        this.esOperations = esOperations;
    }

    @Override
    public Entity updateBasicInformation(EntityType entityType, String entityId, EntityBasicInformationUpdateRequest request) {
        final List<Bson> updates = new ArrayList<>();
        updates.add(set(NAME, request.name));
        updates.add(set(INDUSTRY, request.industry));
        updates.add(set(LOCATION, request.location));
        updates.add(request.website == null || request.website.isEmpty()
                ? unset(WEBSITE)
                : set(WEBSITE, request.website)
        );

        final Entity entity = repository.findOneAndUpdate(entityType, byId(entityId), combine(updates), createReturnNew());
        if(entity.isActive()) esOperations.update(new UpdateEntityDataSpecification(entity));
        return entity;
    }

    @Override
    public Entity updateEntityLogo(Entity entity, String logoUrl) {
        return repository.findOneAndUpdate(
                entity.getType(),
                byId(entity.getId()),
                set(LOGO, logoUrl),
                createReturnNew());
    }

    @Override
    public Entity updateEntityImage(Entity entity, String imageUrl) {
        return repository.findOneAndUpdate(
                entity.getType(),
                byId(entity.getId()),
                set(IMAGE_URL, imageUrl),
                createReturnNew());
    }

    private FindOneAndUpdateOptions createReturnNew() {
        return new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
    }

    private class UpdateEntityDataSpecification implements ElasticSearchModificationSpecification {

        private final EntityMapper mapper = EntityMapper.getInstance();
        private final Entity entity;

        private UpdateEntityDataSpecification(Entity entity) {
            this.entity = entity;
        }

        @Override
        public String getIndex() {
            return entity.getType().toElasticSearchType();
        }

        @Override
        public String getPath() {
            return String.format("doc/%s/_update", entity.getId());
        }

        @Override
        public String getBody() {
            final Document updates = new Document();

            addToUpdates(updates, mapper.updateName(entity.getName()));
            addToUpdates(updates, mapper.updateLocation(entity.getLocation()));

            return String.format("{ \"doc\": %s }", updates.toJson());
        }

        private void addToUpdates(Map<String, Object> updates, Map.Entry<String, Object> update) {
            if(update != null) updates.put(update.getKey(), update.getValue());
        }
    }
}