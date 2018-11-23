package net.readybid.auth.account.core;

import net.readybid.utils.CreationDetails;
import net.readybid.utils.StatusDetails;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public class AccountStatusDetails extends StatusDetails<AccountStatus> {

    public AccountStatusDetails(CreationDetails created, AccountStatus status) {
        super(created, status);
    }

    public AccountStatusDetails() {}
}
