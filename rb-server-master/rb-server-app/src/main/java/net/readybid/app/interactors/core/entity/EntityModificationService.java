package net.readybid.app.interactors.core.entity;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.use_cases.core.entity.EntityBasicInformationUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface EntityModificationService {

    Entity setBasicInformation(EntityType entityType, String entityId, EntityBasicInformationUpdateRequest request);

    Entity updateLogo(EntityType entityType, String entityId, MultipartFile multipartFile);

    Entity updateImage(EntityType entityType, String entityId, MultipartFile multipartFile);
}
