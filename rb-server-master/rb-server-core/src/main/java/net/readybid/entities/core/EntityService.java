package net.readybid.entities.core;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.user.BasicUserDetails;

/**
 * Created by DejanK on 3/24/2017.
 *
 */
public interface EntityService {

    Entity createEntity(CreateEntityRequest entity, BasicUserDetails user);

    Entity getEntityIncludingUnverified(EntityType entityType, String entityId);

    Hotel getHotelIncludingUnverified(String hotelId);

    Entity getEntityIncludingUnverified(String entityId);
}
