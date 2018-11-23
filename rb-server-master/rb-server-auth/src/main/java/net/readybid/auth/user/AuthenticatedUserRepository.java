package net.readybid.auth.user;

import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/23/2017.
 *
 */
public interface AuthenticatedUserRepository {
    AuthenticatedUser get(ObjectId userId);

    void addUserAccount(AuthenticatedUser user, ObjectId userAccountId);

    void setCurrentUserAccount(AuthenticatedUser user);

    void create(AuthenticatedUser authenticatedUser);

    AuthenticatedUser getUserByEmailAddressAndAccountId(String emailAddress, ObjectId accountId);

    AuthenticatedUser getUserByEmailAddressAndAccountId(String emailAddress, String accountId);

    void deleteTutorial(AuthenticatedUser user, String tutorialName);

    boolean isUser(String emailAddress);
}
