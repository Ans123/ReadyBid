package net.readybid.app.persistence.mongodb.repository.implementation;

import net.readybid.app.core.entities.entity.EntityType;

interface EntityRepositoryLibrary {
    void register(EntityType entityType, ShadowEntityRepository shadowEntityRepository);
}
