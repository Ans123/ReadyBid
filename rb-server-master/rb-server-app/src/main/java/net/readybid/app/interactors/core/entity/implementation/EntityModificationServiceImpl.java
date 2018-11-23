package net.readybid.app.interactors.core.entity.implementation;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.EntityModificationService;
import net.readybid.app.use_cases.core.entity.EntityBasicInformationUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service("entityModificationService")
public class EntityModificationServiceImpl implements EntityModificationService, EntityModificationServiceLibrary {

    private final Map<EntityType, EntityModificationService> serviceMap = new HashMap<>();
    private final EntityModificationService defaultEntityModificationService;

    @Autowired
    public EntityModificationServiceImpl(
            @Qualifier("DefaultEntityModificationService") EntityModificationService entityModificationService
    ) {
        this.defaultEntityModificationService = entityModificationService;
    }

    @Override
    public void register(EntityType entityType, EntityModificationService modificationService) {
        if(serviceMap.containsKey(entityType)){
            // TODO: LOG + LOG on Similar classes
            System.out.println(String.format("Service for type %s is already registered", entityType));
        }
        serviceMap.put(entityType, modificationService);
    }

    @Override
    public Entity setBasicInformation(EntityType entityType, String entityId, EntityBasicInformationUpdateRequest request) {
        return serviceMap.getOrDefault(entityType, defaultEntityModificationService)
                .setBasicInformation(entityType, entityId, request);
    }

    @Override
    public Entity updateLogo(EntityType entityType, String entityId, MultipartFile multipartFile) {
        return serviceMap.getOrDefault(entityType, defaultEntityModificationService)
                .updateLogo(entityType, entityId, multipartFile);
    }

    @Override
    public Entity updateImage(EntityType entityType, String entityId, MultipartFile multipartFile) {
        return serviceMap.getOrDefault(entityType, defaultEntityModificationService)
                .updateImage(entityType, entityId, multipartFile);
    }
}
