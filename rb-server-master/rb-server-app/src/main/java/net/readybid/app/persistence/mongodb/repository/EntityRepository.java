package net.readybid.app.persistence.mongodb.repository;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import org.bson.conversions.Bson;

public interface EntityRepository {
    Entity findOne(EntityType entityType, Bson filter, Bson projection, boolean includeUnvalidated);

    Entity findOneAndUpdate(EntityType entityType, Bson query, Bson update, FindOneAndUpdateOptions options);
}
