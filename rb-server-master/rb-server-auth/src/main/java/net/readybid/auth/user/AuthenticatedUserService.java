package net.readybid.auth.user;

import org.bson.types.ObjectId;

/**
 * Created by DejanK on 5/20/2017.
 *
 */
public interface AuthenticatedUserService {
    AuthenticatedUser getUserByEmailAddressAndAccountId(String emailAddress, ObjectId accountId);

    void disableTutorial(AuthenticatedUser currentUser, String tutorialName);

    boolean isUser(String emailAddress);

    void switchToAccount(AuthenticatedUser currentUser, String accountId);

    void switchToUserAccount(AuthenticatedUser user, ObjectId objectId);
}
