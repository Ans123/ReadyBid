package net.readybid.app.persistence.mongodb.repository.implementation;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import net.readybid.app.core.entities.entity.Entity;
import org.bson.conversions.Bson;

interface ShadowEntityRepository {

    Entity findOne(Bson filter, Bson projection, boolean includeUnvalidated);

    Entity findOneAndUpdate(Bson query, Bson update, FindOneAndUpdateOptions options);
}
