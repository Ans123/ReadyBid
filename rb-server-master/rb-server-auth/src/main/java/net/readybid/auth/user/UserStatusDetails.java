package net.readybid.auth.user;

import net.readybid.utils.CreationDetails;
import net.readybid.utils.StatusDetails;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public class UserStatusDetails extends StatusDetails<UserStatus>{
    public UserStatusDetails() {}

    public UserStatusDetails(CreationDetails creationDetails, UserStatus userStatus) {
        super(creationDetails, userStatus);
    }

    public UserStatusDetails(UserStatus userStatus) {
        super(userStatus);
    }

    public boolean isActive() {
        return value.isActive();
    }
}
