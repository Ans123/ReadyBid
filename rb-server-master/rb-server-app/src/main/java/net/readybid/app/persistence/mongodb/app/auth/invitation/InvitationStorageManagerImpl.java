package net.readybid.app.persistence.mongodb.app.auth.invitation;

import net.readybid.app.interactors.authentication.invitation.gate.InvitationStorageManager;
import net.readybid.app.persistence.mongodb.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static net.readybid.app.persistence.mongodb.repository.mapping.InvitationCollection.ACCOUNT_ID;
import static net.readybid.app.persistence.mongodb.repository.mapping.InvitationCollection.ACCOUNT_NAME;
import static net.readybid.mongodb.RbMongoFilters.oid;

@Service
public class InvitationStorageManagerImpl implements InvitationStorageManager {

    private final InvitationRepository repository;

    @Autowired
    public InvitationStorageManagerImpl(InvitationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateAccountName(String accountId, String name) {
        repository.update(eq(ACCOUNT_ID, oid(accountId)), set(ACCOUNT_NAME, name));
    }
}
