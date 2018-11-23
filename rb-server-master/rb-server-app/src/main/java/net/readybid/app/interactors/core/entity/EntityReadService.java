package net.readybid.app.interactors.core.entity;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;

public interface EntityReadService {

    Entity get(EntityType type, String entityId);
}
