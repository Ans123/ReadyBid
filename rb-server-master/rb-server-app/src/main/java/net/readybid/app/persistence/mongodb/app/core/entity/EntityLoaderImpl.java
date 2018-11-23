package net.readybid.app.persistence.mongodb.app.core.entity;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.gate.EntityLoader;
import net.readybid.app.persistence.mongodb.repository.EntityRepository;
import net.readybid.app.persistence.mongodb.repository.mapping.EntityCollection;
import net.readybid.mongodb.MongoRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Projections.include;
import static net.readybid.mongodb.RbMongoFilters.byId;

@Service
class EntityLoaderImpl implements EntityLoader {

    private final EntityRepository repository;

    @Autowired
    EntityLoaderImpl(EntityRepository repository) {
        this.repository = repository;
    }

    @Override
    @MongoRetry
    public Entity load(EntityType entityType, String entityId) {
        return repository.findOne(
                entityType,
                byId(entityId),
                include(EntityCollection.ALL_FIELDS),
                EntityCollection.INCLUDE_UNVALIDATED
        );
    }

}
