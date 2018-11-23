package net.readybid.entities.core;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 11/10/2016.
 *
 */
public interface EntityRepository {

    void saveForValidation(Entity entity);

    Entity findByIdIncludingUnverified(EntityType entityType, String entityId);

    Entity findByIdIncludingUnverified(EntityType entityType, ObjectId entityId);

    Hotel getHotelIncludingUnverified(String hotelId);

    Entity findByIdIncludingUnverified(String entityId);

    Entity findByIdIncludingUnverified(ObjectId entityId);
}
