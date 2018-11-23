package net.readybid.auth.permissions;

import net.readybid.auth.permission.Permission;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by DejanK on 4/6/2017.
 *
 */
public interface Permissions extends Map<String, Set<Permission>> {
    List<ObjectId> getAccountIdsWithPermission(Permission permission);

    boolean hasPermission(String accountId, Permission permission);
}
