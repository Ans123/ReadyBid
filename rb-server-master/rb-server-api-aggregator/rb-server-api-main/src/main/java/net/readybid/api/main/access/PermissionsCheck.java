package net.readybid.api.main.access;

import net.readybid.app.entities.authorization.dirty.InvolvedAccounts;
import net.readybid.auth.user.AuthenticatedUser;

/**
 * Created by DejanK on 4/7/2017.
 *
 */
public interface PermissionsCheck {
    boolean check(AuthenticatedUser user, InvolvedAccounts involvedAccounts);
}
