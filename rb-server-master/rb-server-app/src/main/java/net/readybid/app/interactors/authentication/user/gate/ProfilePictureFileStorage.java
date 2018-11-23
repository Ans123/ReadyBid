package net.readybid.app.interactors.authentication.user.gate;

import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureFileStorage {
    String put(String userId, MultipartFile multipartFile);

    void delete(String imageUrl);
}
