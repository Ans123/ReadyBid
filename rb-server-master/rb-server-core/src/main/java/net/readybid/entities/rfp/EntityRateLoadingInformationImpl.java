package net.readybid.entities.rfp;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import org.bson.types.ObjectId;

import java.util.List;

public class EntityRateLoadingInformationImpl implements EntityRateLoadingInformation {

    private ObjectId entityId;
    private EntityType entityType;
    private Entity entity;
    private List<? extends RateLoadingInformation> information;

    @Override
    public ObjectId getEntityId() {
        return entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public String getEntityName() {
        return entity == null ? "" : entity.getName();
    }

    @Override
    public List<? extends RateLoadingInformation> getInformation() {
        return information;
    }

    @Override
    public boolean containsInformation() {
        return information != null && !information.isEmpty();
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        this.entityId = entity.getId();
        this.entityType = entity.getType();
    }

    public void setEntityId(ObjectId entityId) {
        this.entityId = entityId;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void setInformation(List<? extends RateLoadingInformation> information) {
        this.information = information;
    }
}
