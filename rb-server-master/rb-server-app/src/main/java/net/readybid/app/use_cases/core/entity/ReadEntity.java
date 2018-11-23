package net.readybid.app.use_cases.core.entity;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.GetEntityResponse;

public interface ReadEntity {

    GetEntityResponse get(EntityType entityType, String entityId);
}
