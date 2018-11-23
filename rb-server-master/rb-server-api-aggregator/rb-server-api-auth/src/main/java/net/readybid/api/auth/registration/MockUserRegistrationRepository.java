package net.readybid.api.auth.registration;

import net.readybid.auth.user.UserRegistration;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface MockUserRegistrationRepository {
    UserRegistration findUserRegistration(String emailAddress);

    void createUserRegistration(UserRegistration userRegistration);

    void recreateUserRegistration(UserRegistration userRegistration);

    void updateUserStatus(UserRegistration userRegistration);

    void updatePassword(ObjectId userId, String password);

    void createUserAndUserAccount(UserRegistration userRegistration);

    void createOrUpdateUser(UserRegistration userRegistration);

    UserRegistration getFullUserRegistrationByEmail(String emailAddress);
}
