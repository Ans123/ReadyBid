package net.readybid.app.use_cases.core.entity;

import net.readybid.app.core.entities.entity.EntityType;
import org.springframework.web.multipart.MultipartFile;

public interface UpdateEntity {

    void setBasicInformation(EntityType entityType, String entityId, EntityBasicInformationUpdateRequest entityBasicInformationUpdateRequest);

    String setLogo(EntityType entityType, String entityId, MultipartFile multipartFile);

    String setImage(EntityType entityType, String entityId, MultipartFile multipartFile);
}
