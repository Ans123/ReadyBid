package net.readybid.auth.account.core;


import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.entities.core.EntityService;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.mongodb.RbDuplicateKeyException;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final EntityService entityService;
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;

    @Autowired
    public AccountServiceImpl(EntityService entityService, AccountRepository accountRepository, AccountFactory accountFactory) {
        this.entityService = entityService;
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
    }

    @Override
    public Account createAccount(CreateEntityRequest createEntityRequest, BasicUserDetails user) {
        final Entity entity = entityService.createEntity(createEntityRequest, user);
        return createAccountForEntity(entity, user);
    }

    @Override
    public Account getOrCreateAccount(EntityType entityType, String entityId, BasicUserDetails user) {
        Account account = accountRepository.getAccountByEntityId(entityId);
        if(account == null){
            try {
                final Entity entity = entityService.getEntityIncludingUnverified(entityType, entityId);
                account = createAccountForEntity(entity, user);
            } catch(RbDuplicateKeyException dke){ //
                account = getOrCreateAccount(entityType, entityId, user);
            }
        }
        return account;
    }

    @Override
    public Account createAccountForEntity(Entity entity, BasicUserDetails currentUser) {
        if(entity == null) throw new NotFoundException();
        final Account account = accountFactory.createAccount(entity, currentUser);
        accountRepository.createAccountForEntity(account);
        return account;
    }

    @Override
    public Account getAccount(ObjectId accountId) {
        return accountRepository.getById(accountId);
    }

    @Override
    public Account getOrCreateAccount(Entity entity, AuthenticatedUser currentUser) {
        Account account = accountRepository.getAccountByEntityId(entity.getId());
        if(account == null){
            try {
                account = createAccountForEntity(entity, currentUser);
            } catch(RbDuplicateKeyException dke){ //
                account = getOrCreateAccount(entity, currentUser);
            }
        }
        return account;
    }

    @Override
    public Account getAccount(String accountId) {
        return accountId != null && ObjectId.isValid(accountId) ? getAccount(new ObjectId(accountId)) : null;
    }
}
