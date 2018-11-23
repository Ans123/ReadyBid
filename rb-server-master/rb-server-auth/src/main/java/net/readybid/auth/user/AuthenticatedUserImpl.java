package net.readybid.auth.user;

import net.readybid.auth.authorization.AuthorizationTokenClaims;
import net.readybid.auth.permission.Permission;
import org.apache.commons.lang3.NotImplementedException;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by DejanK on 3/23/2017.
 *
 */
public class AuthenticatedUserImpl extends UserImpl implements AuthenticatedUser {

    private AuthorizationTokenClaims credentials;

    @Override
    public Object getDetails() {
        return this;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        throw new NotImplementedException("not needed");
    }

    @Override
    public AuthorizationTokenClaims getCredentials() {
        return credentials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        throw new NotImplementedException("not needed");
    }

    @Override
    public void setCredentials(AuthorizationTokenClaims claims) {
        this.credentials = claims;
    }

    @Override
    public boolean hasChangedSince(long timestamp) {
        return timestamp < getLatestChangeTimestamp();
    }


    @Override
    public boolean hasPermission(ObjectId accountId, Permission permission) {
        return currentUserAccount != null && currentUserAccount.hasPermission(accountId, permission);
    }

    @Override
    public List<ObjectId> getAccountIdsWithPermission(Permission permission) {
        return currentUserAccount == null ? new ArrayList<>() : currentUserAccount.getAccountIdsWithPermission(permission);
    }

    @Override
    public String getName() {
        return getFullName();
    }
}
