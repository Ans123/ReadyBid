package net.readybid.auth.user;


import net.readybid.FilePaths;
import net.readybid.auth.useraccount.web.UserAccountViewModel;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public class AuthenticatedUserViewModel implements ViewModel<AuthenticatedUser> {

    public static final ViewModelFactory<AuthenticatedUser, AuthenticatedUserViewModel> FACTORY = AuthenticatedUserViewModel::new;

    public ObjectId id;
    public String firstName;
    public String lastName;
    public String emailAddress;
    public String phone;
    public String profilePicture;
    public List<String> tutorials;
    public int userAccounts;

    public UserAccountViewModel currentUserAccount;
    public UserStatus status;

    public Long changed;

    public AuthenticatedUserViewModel(AuthenticatedUser authenticatedUser) {
        id = authenticatedUser.getId();
        firstName = authenticatedUser.getFirstName();
        lastName = authenticatedUser.getLastName();
        emailAddress = authenticatedUser.getEmailAddress();
        phone = authenticatedUser.getPhone();
        profilePicture = getProfilePictureFileNameWithPath(authenticatedUser.getProfilePicture());
        tutorials = authenticatedUser.getTutorials();
        userAccounts = authenticatedUser.getUserAccounts() == null ? 0 : authenticatedUser.getUserAccounts().size();

        currentUserAccount = UserAccountViewModel.FACTORY.createAsPartial(authenticatedUser.getCurrentUserAccount());
        status = authenticatedUser.getStatusValue();
        changed = authenticatedUser.getLastChangeTimestamp();
    }

    private String getProfilePictureFileNameWithPath(String profilePictureFileName) {
        return profilePictureFileName == null || profilePictureFileName.isEmpty() ? null : FilePaths.PROFILE_PICTURE + profilePictureFileName;
    }
}
