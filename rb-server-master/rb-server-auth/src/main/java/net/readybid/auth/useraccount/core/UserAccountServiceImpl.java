package net.readybid.auth.useraccount.core;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.web.UserAccountService;
import net.readybid.auth.useraccount.web.UserAccountViewModel;
import net.readybid.utils.ListResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/24/2017.
 *
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountServiceImpl(
            UserAccountRepository userAccountRepository
    ) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public ListResult<UserAccountViewModel> listUserAccountsForUser(AuthenticatedUser currentUser) {
        return userAccountRepository.listUserAccountsForUser(currentUser);
    }

    @Override
    public UserAccount getByUserAndAccount(ObjectId userId, ObjectId accountId) {
        return userAccountRepository.getUserAccountByUserIdAndAccountId(userId, accountId);
    }

    @Override
    public UserAccount getById(String id) {
        return userAccountRepository.getById(id);
    }

    @Override
    public UserAccount getById(ObjectId objectId) {
        return userAccountRepository.getById(objectId);
    }

    @Override
    public void updateLastUsedBidManager(AuthenticatedUser user, String viewId) {
        final UserAccount account = user.getCurrentUserAccount();
        if(ObjectId.isValid(viewId) && account != null){
            final ObjectId objectViewId = new ObjectId(viewId);

            if(account.shouldUpdateLastBidManagerView(objectViewId)) {
                account.setLastUsedBidManager(objectViewId);
                userAccountRepository.updateLastUsedBidManager(account);
            }
        }
    }
}
