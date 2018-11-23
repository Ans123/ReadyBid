package net.readybid.app.use_cases.authentication.user.implementation;

import net.readybid.app.interactors.authentication.user.BasicInformation;
import net.readybid.app.interactors.authentication.user.UserModificationService;
import net.readybid.app.interactors.rfp.RfpModificationService;
import net.readybid.app.use_cases.authentication.user.UpdateUser;
import net.readybid.auth.password.PasswordService;
import net.readybid.auth.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UpdateUserImpl implements UpdateUser {

    private final UserModificationService userModificationService;
    private final RfpModificationService rfpModificationService;
    private final PasswordService passwordService;

    @Autowired
    public UpdateUserImpl(
            UserModificationService userModificationService,
            RfpModificationService rfpModificationService,
            PasswordService passwordService
    ) {
        this.userModificationService = userModificationService;
        this.rfpModificationService = rfpModificationService;
        this.passwordService = passwordService;
    }

    @Override
    public void setBasicInformation(String userId, BasicInformation basicInformation) {
        final User updatedUser = userModificationService.setBasicDetails(userId, basicInformation);
        rfpModificationService.handleUserDetailsChange(updatedUser);
    }

    @Override
    public void setEmailAddress(String userId, String emailAddress) {
        final User user = userModificationService.setEmailAddress(userId, emailAddress);
        rfpModificationService.handleUserEmailAddressChange(user);
    }

    @Override
    public void setPassword(String userId, String password) {
        final String encodedPassword = passwordService.encode(password);
        userModificationService.setPassword(userId, encodedPassword);
    }

    @Override
    public void setProfilePicture(User user, MultipartFile multipartFile) {
        final User updatedUser = userModificationService.setProfilePicture(user, multipartFile);
        rfpModificationService.handleUserProfilePictureChange(updatedUser);
    }
}
