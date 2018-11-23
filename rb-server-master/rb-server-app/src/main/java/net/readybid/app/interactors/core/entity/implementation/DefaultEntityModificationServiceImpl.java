package net.readybid.app.interactors.core.entity.implementation;


import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.interactors.core.entity.EntityModificationService;
import net.readybid.app.interactors.core.entity.gate.EntityImageFileStorage;
import net.readybid.app.interactors.core.entity.gate.EntityLoader;
import net.readybid.app.interactors.core.entity.gate.EntityStorageManager;
import net.readybid.app.interactors.core.entity.gate.LogoFileStorage;
import net.readybid.app.use_cases.core.entity.EntityBasicInformationUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("DefaultEntityModificationService")
class DefaultEntityModificationServiceImpl implements EntityModificationService {

    private final LogoFileStorage logoFileStorage;
    private final EntityImageFileStorage entityImageFileStorage;

    private final EntityLoader loader;
    private final EntityStorageManager storageManager;

    @Autowired
    DefaultEntityModificationServiceImpl(
            LogoFileStorage logoFileStorage,
            EntityImageFileStorage entityImageFileStorage,
            EntityLoader loader,
            EntityStorageManager storageManager
    ) {
        this.logoFileStorage = logoFileStorage;
        this.entityImageFileStorage = entityImageFileStorage;
        this.loader = loader;
        this.storageManager = storageManager;
    }

    @Override
    public Entity setBasicInformation(EntityType entityType, String entityId, EntityBasicInformationUpdateRequest request){
        final Entity originalEntity = loader.load(entityType, entityId);
        if(originalEntity == null) throw new NotFoundException();
        if(request.hasDifferences(originalEntity)){
            final Entity entity = storageManager.updateBasicInformation(entityType, entityId, request);
            if(entity == null) throw new NotFoundException();
            return entity;
        }
        return null;
    }

    @Override
    public Entity updateLogo(EntityType entityType, String entityId, MultipartFile multipartFile) {
        final Entity originalEntity = loader.load(entityType, entityId);
        if(originalEntity == null) throw new NotFoundException();
        final String logoUrl = logoFileStorage.put(entityId, multipartFile);
        final Entity entity = storageManager.updateEntityLogo(originalEntity, logoUrl);
        if(entity == null) {
            logoFileStorage.delete(logoUrl);
            throw new NotFoundException();
        }
        logoFileStorage.delete(originalEntity.getLogo());
        return entity;
    }

    @Override
    public Entity updateImage(EntityType entityType, String entityId, MultipartFile multipartFile) {
        final Entity originalEntity = loader.load(entityType, entityId);
        if(originalEntity == null) throw new NotFoundException();
        final String imageUrl = entityImageFileStorage.put(entityId, multipartFile);
        final Entity entity = storageManager.updateEntityImage(originalEntity, imageUrl);
        if(entity == null) {
            entityImageFileStorage.delete(imageUrl);
            throw new NotFoundException();
        }
        entityImageFileStorage.delete(originalEntity.getImageUrl());
        return entity;
    }
}
