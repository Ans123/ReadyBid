package net.readybid.app.interactors.core.entity.implementation;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.EntityModificationService;

interface EntityModificationServiceLibrary {
    void register(EntityType entityType, EntityModificationService modificationService);
}
