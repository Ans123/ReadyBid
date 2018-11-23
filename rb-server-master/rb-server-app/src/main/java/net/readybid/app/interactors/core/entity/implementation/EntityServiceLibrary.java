package net.readybid.app.interactors.core.entity.implementation;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.EntityModificationService;
import net.readybid.app.interactors.core.entity.EntityReadService;

interface EntityServiceLibrary {
    void registerModificationService(EntityType entityType, EntityModificationService entityModificationService);
    void registerReadService(EntityType entityType, EntityReadService entityReadService);
}
