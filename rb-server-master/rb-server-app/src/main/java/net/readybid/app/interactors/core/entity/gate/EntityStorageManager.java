package net.readybid.app.interactors.core.entity.gate;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.use_cases.core.entity.EntityBasicInformationUpdateRequest;

public interface EntityStorageManager {

    Entity updateBasicInformation(EntityType entityType, String entityId, EntityBasicInformationUpdateRequest request);

    Entity updateEntityLogo(Entity entity, String logoUrl);

    Entity updateEntityImage(Entity entity, String imageUrl);
}
