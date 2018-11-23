package net.readybid.auth.permissions;

import net.readybid.auth.permission.Permission;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by DejanK on 4/6/2017.
 *
 */
public class PermissionsImpl extends HashMap<String, Set<Permission>> implements Permissions  {

    @Override
    public List<ObjectId> getAccountIdsWithPermission(Permission permission) {
        return keySet().stream().filter(id -> hasPermission(id, permission)).filter(ObjectId::isValid).map(ObjectId::new).collect(Collectors.toList());
    }

    @Override
    public boolean hasPermission(String accountId, Permission permission) {
        return containsKey(accountId) && get(accountId).contains(permission);
    }
}
