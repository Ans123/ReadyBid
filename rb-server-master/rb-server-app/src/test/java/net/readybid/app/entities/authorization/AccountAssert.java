package net.readybid.app.entities.authorization;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.entities.core.LocationAssert;
import net.readybid.auth.account.core.Account;
import net.readybid.test_utils.RbAbstractAssert;

import java.util.function.Consumer;

public class AccountAssert extends RbAbstractAssert<AccountAssert, Account> {

    public static AccountAssert that(Account actual) {
        return new AccountAssert(actual);
    }

    private AccountAssert(Account actual) {
        super(actual, AccountAssert.class);
    }

    public AccountAssert hasAccountId(Object expected) {
        assertFieldEquals("account id", String.valueOf(actual.getId()), String.valueOf(expected));
        return this;
    }

    public AccountAssert hasEntityId(Object expected) {
        assertFieldEquals("entity id", String.valueOf(actual.getEntityId()), String.valueOf(expected));
        return this;
    }

    public AccountAssert hasType(Object expected) {
        assertFieldEquals("type", actual.getType(), EntityType.valueOf(String.valueOf(expected)));
        return this;
    }

    public AccountAssert hasName(Object expected) {
        assertFieldEquals("name", actual.getName(), expected);
        return this;
    }

    public AccountAssert hasWebsite(Object expected) {
        assertFieldEquals("website", actual.getWebsite(), expected);
        return this;
    }

    public AccountAssert hasEmailAddress(Object expected) {
        assertFieldEquals("email address", actual.getEmailAddress(), expected);
        return this;
    }

    public AccountAssert hasPhone(Object expected) {
        assertFieldEquals("phone", actual.getPhone(), expected);
        return this;
    }

    public AccountAssert hasLogo(Object expected) {
        assertFieldEquals("logo", actual.getLogo(), expected);
        return this;
    }

    public AccountAssert hasLocation(Consumer<LocationAssert> consumer) {
        consumer.accept(LocationAssert.that(actual.getLocation()));
        return this;
    }
}
