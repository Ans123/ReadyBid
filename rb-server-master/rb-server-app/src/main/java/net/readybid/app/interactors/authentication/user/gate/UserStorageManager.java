package net.readybid.app.interactors.authentication.user.gate;

import net.readybid.app.interactors.authentication.user.BasicInformation;
import net.readybid.auth.user.User;

public interface UserStorageManager {
    User updateBasicDetails(String userId, BasicInformation basicInformation);

    User updateEmailAddress(String userId, String emailAddress);

    void updatePassword(String userId, String password);

    User updateProfilePicture(String userId, String pictureUrl);
}
