package net.readybid.app.interactors.core.entity.implementation;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.core.entity.EntityModificationService;
import net.readybid.app.interactors.core.entity.gate.HotelChainLoader;
import net.readybid.app.interactors.core.entity.gate.HotelChainStorageManager;
import net.readybid.app.interactors.core.entity.gate.HotelStorageManager;
import net.readybid.app.use_cases.core.entity.EntityBasicInformationUpdateRequest;
import net.readybid.entities.chain.HotelChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HotelChainModificationServiceImpl implements EntityModificationService {

    private final EntityModificationService defaultEntityModificationService;
    private final HotelChainLoader loader;
    private final HotelChainStorageManager chainStorageManager;
    private final HotelStorageManager hotelStorageManager;

    @Autowired
    public HotelChainModificationServiceImpl(
            EntityModificationServiceLibrary entityModificationServiceLibrary,
            @Qualifier("DefaultEntityModificationService") EntityModificationService defaultEntityModificationService,
            HotelChainLoader loader,
            HotelChainStorageManager chainStorageManager,
            HotelStorageManager hotelStorageManager
    ) {
        this.loader = loader;
        this.chainStorageManager = chainStorageManager;
        this.hotelStorageManager = hotelStorageManager;
        this.defaultEntityModificationService = defaultEntityModificationService;
        entityModificationServiceLibrary.register(EntityType.CHAIN, this);
    }

    @Override
    public Entity setBasicInformation(EntityType entityType, String entityId, EntityBasicInformationUpdateRequest request) {
        final Entity entity = defaultEntityModificationService.setBasicInformation(entityType, entityId, request);
        final HotelChain chain = loader.load(String.valueOf(entity.getId()));
        if(chain.isMasterChain()){
            chainStorageManager.setBrandMasterChainBasicDetails(chain);
            hotelStorageManager.setMasterChainBasicDetails(chain);
        } else {
            hotelStorageManager.setBrandBasicDetails(chain);
        }

        return entity;
    }

    @Override
    public Entity updateLogo(EntityType entityType, String entityId, MultipartFile multipartFile) {
        return defaultEntityModificationService.updateLogo(entityType, entityId, multipartFile);
    }

    @Override
    public Entity updateImage(EntityType entityType, String entityId, MultipartFile multipartFile) {
        return defaultEntityModificationService.updateImage(entityType, entityId, multipartFile);
    }
}
