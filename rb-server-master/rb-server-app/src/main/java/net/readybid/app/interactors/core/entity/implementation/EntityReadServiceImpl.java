package net.readybid.app.interactors.core.entity.implementation;


import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.EntityReadService;
import net.readybid.app.interactors.core.entity.gate.EntityLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class EntityReadServiceImpl implements EntityReadService {

    private final EntityLoader loader;

    @Autowired
    EntityReadServiceImpl(EntityLoader loader) {
        this.loader = loader;
    }

    @Override
    public Entity get(EntityType type, String entityId) {
        return loader.load(type, entityId);
    }
}
