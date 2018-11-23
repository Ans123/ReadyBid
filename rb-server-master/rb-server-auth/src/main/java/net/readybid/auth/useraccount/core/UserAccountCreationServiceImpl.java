package net.readybid.auth.useraccount.core;

import net.readybid.auth.account.core.Account;
import net.readybid.auth.account.core.AccountService;
import net.readybid.auth.bid.AuthBidService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.AuthenticatedUserRepository;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.web.AddUserAccountRequest;
import net.readybid.auth.useraccount.web.CreateAccountRequest;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.mongodb.RbDuplicateKeyException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 5/19/2017.
 *
 */
@Service
public class UserAccountCreationServiceImpl implements UserAccountCreationService {

    private final AccountService accountService;
    private final UserAccountFactory userAccountFactory;
    private final UserAccountRepository userAccountRepository;
    private final AuthenticatedUserRepository authenticatedUserRepository;
    private final AuthBidService authBidService;
    private final UserAccountCreationEmailService userAccountCreationEmailService;

    @Autowired
    public UserAccountCreationServiceImpl(
            AccountService accountService,
            UserAccountFactory userAccountFactory,
            UserAccountRepository userAccountRepository,
            AuthenticatedUserRepository authenticatedUserRepository,
            AuthBidService authBidService,
            UserAccountCreationEmailService userAccountCreationEmailService
    ) {
        this.accountService = accountService;
        this.userAccountFactory = userAccountFactory;
        this.userAccountRepository = userAccountRepository;
        this.authenticatedUserRepository = authenticatedUserRepository;
        this.authBidService = authBidService;
        this.userAccountCreationEmailService = userAccountCreationEmailService;
    }

    @Override
    public ObjectId createUserAccount(CreateAccountRequest createAccountRequest, AuthenticatedUser user) {
        final Account account = accountService.createAccount(createAccountRequest.entity, user);
        final UserAccount userAccount = userAccountFactory.createUserAccount(account, user, createAccountRequest.jobTitle);
        return addUserAccount(userAccount, user).getAccountId();
    }

    @Override
    public ObjectId addUserAccount(AddUserAccountRequest addUserAccountRequest, AuthenticatedUser user) {
        final Account account = accountService.getOrCreateAccount(addUserAccountRequest.entityType, addUserAccountRequest.entityId, user);
        final UserAccount userAccount = userAccountFactory.createUserAccount(account, user, addUserAccountRequest.jobTitle);
        return addUserAccount(userAccount, user).getAccountId();
    }

    public void onNewUserAccount(UserAccount userAccount){
        notifyJoe(userAccount);
        authBidService.onNewUserAccount(userAccount);
    }

    private void notifyJoe(UserAccount userAccount) {
        userAccountCreationEmailService.notifyAbout(userAccount);
    }

    private UserAccount addUserAccount(UserAccount userAccount, AuthenticatedUser user) {
        saveNewAccount(userAccount);
        authenticatedUserRepository.addUserAccount(user, userAccount.getId());
        onNewUserAccount(userAccount);
        return userAccount;
    }

    private void saveNewAccount(UserAccount userAccount) {
        try{
            userAccountRepository.create(userAccount);
        } catch (RbDuplicateKeyException dke){
            throw new UnableToCompleteRequestException("DUPLICATE_USER_ACCOUNT");
        }
    }
}
