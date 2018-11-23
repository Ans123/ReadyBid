package net.readybid.auth.useraccount.core;

import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.User;
import net.readybid.auth.useraccount.UserAccountStatus;
import net.readybid.auth.useraccount.UserAccountStatusDetails;
import net.readybid.bidmanagerview.BidManagerView;
import net.readybid.bidmanagerview.BidManagerViewCreationService;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/27/2017.
 *
 */
@Service
public class UserAccountFactoryImpl implements UserAccountFactory {

    private final BidManagerViewCreationService bidManagerViewCreationService;

    @Autowired
    public UserAccountFactoryImpl(BidManagerViewCreationService bidManagerViewCreationService) {
        this.bidManagerViewCreationService = bidManagerViewCreationService;
    }

    @Override
    public UserAccountImpl createUserAccount(Account account, User user, String jobTitle) {
        return createUserAccount(account, user, jobTitle, UserAccountStatus.UNVERIFIED);
    }

    @Override
    public UserAccountImpl createUserAccountFromInvitation(User user, Account account, String jobTitle) {
        return createUserAccount(account, user, jobTitle, UserAccountStatus.ACTIVE);
    }

    private UserAccountImpl createUserAccount(Account account, User user, String jobTitle, UserAccountStatus status) {
        final UserAccountImpl userAccount = new UserAccountImpl();

        userAccount.setId(new ObjectId());
        userAccount.setUser(user);
        userAccount.setAccount(account);
        userAccount.setJobTitle(jobTitle);

        userAccount.setCreated(new CreationDetails(null));
        userAccount.setStatus(new UserAccountStatusDetails(userAccount.getCreated(), status));

        final BidManagerView view = bidManagerViewCreationService.createDefaultViewsForUser(userAccount.getAccountType(), userAccount.getAccountId());
        userAccount.setDefaultView(view.getId());
        userAccount.markLastChange();

        return userAccount;
    }
}
