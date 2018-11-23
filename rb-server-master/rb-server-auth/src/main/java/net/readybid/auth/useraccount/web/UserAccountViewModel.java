package net.readybid.auth.useraccount.web;

import net.readybid.auth.account.web.AccountViewModel;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.UserAccountStatus;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/29/2017.
 *
 */
public class UserAccountViewModel implements ViewModel<UserAccount> {

    public static final ViewModelFactory<UserAccount, UserAccountViewModel> FACTORY = UserAccountViewModel::new;

    public ObjectId id;
    public ObjectId userId;
    public AccountViewModel account;
    public String jobTitle;
    public UserAccountStatus status;
    public Long changed;
    public ObjectId defaultBidManagerViewId;
    public ObjectId lastBidManagerViewId;

    public UserAccountViewModel(UserAccount userAccount) {
        id = userAccount.getId();
        userId = userAccount.getUserId();
        account = AccountViewModel.FACTORY.createAsPartial(userAccount.getAccount());
        jobTitle = userAccount.getJobTitle();
        status = userAccount.getStatusValue();
        changed = userAccount.getLastChangeTimestamp();
        defaultBidManagerViewId = userAccount.getDefaultBidManagerView();
        lastBidManagerViewId = userAccount.getLastUsedBidManagerView();
    }
}
