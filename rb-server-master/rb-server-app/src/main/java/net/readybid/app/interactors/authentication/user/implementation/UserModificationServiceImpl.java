package net.readybid.app.interactors.authentication.user.implementation;

import net.readybid.app.interactors.authentication.user.BasicInformation;
import net.readybid.app.interactors.authentication.user.UserModificationService;
import net.readybid.app.interactors.authentication.user.gate.ProfilePictureFileStorage;
import net.readybid.app.interactors.authentication.user.gate.UserStorageManager;
import net.readybid.auth.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
class UserModificationServiceImpl implements UserModificationService {

    private final ProfilePictureFileStorage profilePictureFileStorage;
    private final UserStorageManager userStorageManager;

    @Autowired
    UserModificationServiceImpl(
            ProfilePictureFileStorage profilePictureFileStorage,
            UserStorageManager userStorageManager
    ) {
        this.profilePictureFileStorage = profilePictureFileStorage;
        this.userStorageManager = userStorageManager;
    }

    @Override
    public User setBasicDetails(String userId, BasicInformation basicInformation) {
        return userStorageManager.updateBasicDetails(userId, basicInformation);
    }

    @Override
    public User setEmailAddress(String userId, String emailAddress) {
        return userStorageManager.updateEmailAddress(userId, emailAddress);
    }

    @Override
    public void setPassword(String userId, String password) {
        userStorageManager.updatePassword(userId, password);
    }

    @Override
    public User setProfilePicture(User user, MultipartFile multipartFile) {
        final String userId = String.valueOf(user.getId());
        final String pictureUrl = profilePictureFileStorage.put(userId, multipartFile);
        profilePictureFileStorage.delete(user.getProfilePicture());
        return userStorageManager.updateProfilePicture(userId, pictureUrl);
    }
}
