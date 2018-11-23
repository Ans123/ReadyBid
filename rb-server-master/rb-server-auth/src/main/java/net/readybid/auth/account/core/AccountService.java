package net.readybid.auth.account.core;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public interface AccountService {

    Account createAccount(CreateEntityRequest createEntityRequest, BasicUserDetails user);

    Account getOrCreateAccount(EntityType entityType, String entityId, BasicUserDetails user);

    Account createAccountForEntity(Entity entity, BasicUserDetails currentUser);

    Account getAccount(ObjectId accountId);

    Account getOrCreateAccount(Entity entity, AuthenticatedUser currentUser);

    Account getAccount(String accountId);
}
