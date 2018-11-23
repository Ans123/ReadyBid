package net.readybid.auth.user;

import net.readybid.auth.useraccount.UserAccount; /**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface UserRegistration extends User {

    String getPassword();

    void addUserAccount(UserAccount userAccount);
}
