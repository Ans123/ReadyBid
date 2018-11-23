package net.readybid.app.interactors.core.entity.gate;

import org.springframework.web.multipart.MultipartFile;

public interface EntityImageFileStorage {
    String put(String entityId, MultipartFile multipartFile);

    void delete(String imageUrl);
}
