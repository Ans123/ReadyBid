package net.readybid.auth.useraccount;

import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetails;
import net.readybid.utils.StatusDetails;

/**
 * Created by DejanK on 3/27/2017.
 *
 */
public class UserAccountStatusDetails extends StatusDetails<UserAccountStatus> {

    public UserAccountStatusDetails(CreationDetails created, UserAccountStatus status) {
        super(created, status);
    }

    public UserAccountStatusDetails() {}

    public UserAccountStatusDetails(BasicUserDetails userDetails, UserAccountStatus status) {
        super(userDetails, status);
    }

    public boolean isActive() {
        return value != null && value.isActive();
    }
}
