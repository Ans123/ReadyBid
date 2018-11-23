package net.readybid.auth.permission;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DejanK on 4/6/2017.
 *
 */
public enum Permission {

    BUYER_CREATE,
    BUYER_READ,
    BUYER_UPDATE,
    BUYER_DELETE,

    SUPPLIER_READ,
    SUPPLIER_UPDATE,
    SUPPLIER_DELETE;

    public static Set<Permission> allowAll() {
        final Set<Permission> all = new HashSet<>();
        all.addAll(Arrays.asList(Permission.values()));
        return all;
    }
}
