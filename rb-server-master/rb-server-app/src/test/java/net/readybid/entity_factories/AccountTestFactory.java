package net.readybid.entity_factories;

import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.auth.account.core.AccountImpl;
import net.readybid.auth.permission.Permission;
import net.readybid.auth.permissions.Permissions;
import net.readybid.auth.permissions.PermissionsImpl;
import net.readybid.test_utils.RbRandom;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class AccountTestFactory {

    private AccountTestFactory(){}

    public static AccountImpl random() {

        final AccountImpl account = new AccountImpl();

        account.setId(RbRandom.oid());
        account.setEntityId(RbRandom.oid());
        account.setName(RbRandom.name());
        account.setType(RbRandom.randomEnum(EntityType.class));
        account.setIndustry(RbRandom.randomEnum(EntityIndustry.class));
        account.setLocation(LocationTestFactory.random());

        account.setWebsite(RbRandom.alphanumeric(20, false));
        account.setLogo(RbRandom.alphanumeric(20, false));
        account.setEmailAddress(RbRandom.emailAddress());
        account.setPhone(RbRandom.phone());

        account.setCreated(CreationDetailsTestFactory.random());
        account.setStatus(AccountStatusDetailsTestFactory.random());
        account.setLastChanged(new Date().getTime());

        account.setPermissions(new PermissionsImpl());
        account.setPrimaryRepresentativeUserAccountId(RbRandom.oid());

        return account;
    }

    public static AccountImpl random(EntityType type, String accountId, Permission permission) {
        final AccountImpl account = random();
        final Permissions permissions = new PermissionsImpl();
        permissions.put(accountId, new HashSet<>(Collections.singletonList(permission)));

        account.setType(type);
        account.setPermissions(permissions);

        return account;
    }
}
