package net.readybid.auth.user;

import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.web.UserAccountService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 5/20/2017.
 *
 */
@Service
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {

    private final AuthenticatedUserRepository repository;
    private final UserAccountService userAccountService;

    @Autowired
    public AuthenticatedUserServiceImpl(AuthenticatedUserRepository repository, UserAccountService userAccountService) {
        this.repository = repository;
        this.userAccountService = userAccountService;
    }

    @Override
    public AuthenticatedUser getUserByEmailAddressAndAccountId(String emailAddress, ObjectId accountId) {
        return repository.getUserByEmailAddressAndAccountId(emailAddress, accountId);
    }

    @Override
    public void disableTutorial(AuthenticatedUser currentUser, String tutorialName) {
        repository.deleteTutorial(currentUser, tutorialName);
    }

    @Override
    public boolean isUser(String emailAddress) {
        return repository.isUser(emailAddress);
    }

    @Override
    public void switchToAccount(AuthenticatedUser currentUser, String accountId) {
        final UserAccount userAccount = userAccountService.getByUserAndAccount(currentUser.getId(), new ObjectId(accountId));
        currentUser.setCurrentUserAccount(userAccount);
        repository.setCurrentUserAccount(currentUser);
    }

    @Override
    public void switchToUserAccount(AuthenticatedUser user, ObjectId objectId) {
        final UserAccount userAccount = userAccountService.getById(objectId);
        user.setCurrentUserAccount(userAccount);
        repository.setCurrentUserAccount(user);
    }
}
