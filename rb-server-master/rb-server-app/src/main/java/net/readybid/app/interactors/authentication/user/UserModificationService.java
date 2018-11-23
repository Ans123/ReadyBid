package net.readybid.app.interactors.authentication.user;

import net.readybid.auth.user.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserModificationService {
    User setBasicDetails(String userId, BasicInformation basicInformation);

    User setEmailAddress(String userId, String emailAddress);

    void setPassword(String userId, String password);

    User setProfilePicture(User user, MultipartFile multipartFile);
}
