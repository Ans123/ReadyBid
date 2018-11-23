package net.readybid.app.interactors.core.entity.gate;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;

public interface EntityLoader {
    Entity load(EntityType type, String entityId);
}
