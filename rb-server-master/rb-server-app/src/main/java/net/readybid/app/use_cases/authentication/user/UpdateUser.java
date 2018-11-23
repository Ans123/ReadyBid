package net.readybid.app.use_cases.authentication.user;

import net.readybid.app.interactors.authentication.user.BasicInformation;
import net.readybid.auth.user.User;
import org.springframework.web.multipart.MultipartFile;

public interface UpdateUser {
    void setBasicInformation(String userId, BasicInformation basicInformation);

    void setEmailAddress(String userId, String emailAddress);

    void setPassword(String userId, String password);

    void setProfilePicture(User user, MultipartFile multipartFile);
}
