package net.readybid.app.use_cases.core.entity.implementation;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.interactors.core.entity.EntityReadService;
import net.readybid.app.interactors.core.entity.GetEntityResponse;
import net.readybid.app.use_cases.core.entity.ReadEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ReadEntityImpl implements ReadEntity {

    private final EntityReadService entityReadService;

    @Autowired
    ReadEntityImpl(EntityReadService entityReadService) {
        this.entityReadService = entityReadService;
    }

    @Override
    public GetEntityResponse get(EntityType entityType, String entityId) {
        final Entity entity = entityReadService.get(entityType, entityId);
        if(entity == null) throw new NotFoundException();
        return new GetEntityResponse(entity);
    }
}
