package net.readybid.auth.user;

import net.readybid.auth.authorization.AuthorizationTokenClaims;
import net.readybid.auth.permission.Permission;
import net.readybid.auth.useraccount.UserAccount;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Created by DejanK on 3/23/2017.
 *
 */
public interface AuthenticatedUser extends User, Authentication {

    void setCredentials(AuthorizationTokenClaims claims);

    boolean hasChangedSince(long timestamp);

    boolean hasPermission(ObjectId accountId, Permission permission);

    List<ObjectId> getAccountIdsWithPermission(Permission permission);

    void setCurrentUserAccount(UserAccount userAccount);
}
