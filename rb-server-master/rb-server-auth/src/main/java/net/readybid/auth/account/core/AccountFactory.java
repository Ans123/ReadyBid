package net.readybid.auth.account.core;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.user.BasicUserDetails;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public interface AccountFactory {
    Account createAccount(Entity entity, BasicUserDetails user);
}
