package net.readybid.api.auth.registration;

import net.readybid.auth.user.UserImpl;
import net.readybid.auth.user.UserRegistration;
import net.readybid.auth.useraccount.UserAccount;

import java.util.Collections;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public class UserRegistrationImpl extends UserImpl implements UserRegistration {

    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void addFirstUserAccount(UserAccount userAccount) {
        userAccounts = Collections.singleton(userAccount.getId());
        setCurrentUserAccount(userAccount);
    }

    public void addUserAccount(UserAccount userAccount) {
        if(userAccounts == null){
            userAccounts = Collections.singleton(userAccount.getId());
        } else if(!userAccounts.contains(userAccount.getId())){
            userAccounts.add(userAccount.getId());
        }
        setCurrentUserAccount(userAccount);
        markLastChange();
    }
}
