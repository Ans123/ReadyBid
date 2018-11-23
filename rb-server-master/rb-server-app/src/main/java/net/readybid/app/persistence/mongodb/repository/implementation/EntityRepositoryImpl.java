package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.persistence.mongodb.repository.EntityRepository;
import net.readybid.exceptions.UnableToCompleteRequestException;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository("NewEntityRepositoryImpl")
public class EntityRepositoryImpl implements EntityRepository, EntityRepositoryLibrary {

    private Map<EntityType, ShadowEntityRepository> repositoryLibrary = new HashMap<>();

    @Override
    public Entity findOne(EntityType entityType, Bson filter, Bson projection, boolean includeUnvalidated) {
        if(!repositoryLibrary.containsKey(entityType)) throw new UnableToCompleteRequestException(
                String.format("No repository for entity type %s", entityType));
        return repositoryLibrary.get(entityType).findOne(filter,projection,includeUnvalidated);
    }

    @Override
    public Entity findOneAndUpdate(EntityType entityType, Bson query, Bson update, FindOneAndUpdateOptions options) {
        if(!repositoryLibrary.containsKey(entityType)) throw new UnableToCompleteRequestException(
                String.format("No repository for entity type %s", entityType));
        return repositoryLibrary.get(entityType).findOneAndUpdate(query, update, options);
    }

    @Override
    public void register(EntityType entityType, ShadowEntityRepository shadowEntityRepository) {
        if(repositoryLibrary.containsKey(entityType))
            System.out.println(String.format("Repository for entity type %s already registered",  entityType));
        this.repositoryLibrary.put(entityType, shadowEntityRepository);
    }
}
