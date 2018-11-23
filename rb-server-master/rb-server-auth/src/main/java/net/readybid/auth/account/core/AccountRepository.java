package net.readybid.auth.account.core;

import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public interface AccountRepository {

    void createAccountForEntity(Account account);

    Account getById(ObjectId accountId);

    Account getAccountByEntityId(String id);

    Account getAccountByEntityId(ObjectId id);
}
