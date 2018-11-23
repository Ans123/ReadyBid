package net.readybid.app.interactors.authentication.invitation.implementation;

import net.readybid.app.interactors.authentication.invitation.InvitationModificationService;
import net.readybid.app.interactors.authentication.invitation.gate.InvitationStorageManager;
import net.readybid.auth.account.core.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationModificationServiceImpl implements InvitationModificationService {

    private final InvitationStorageManager storageManager;

    @Autowired
    public InvitationModificationServiceImpl(InvitationStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public void updateInvitationsWithAccount(Account account) {
        storageManager.updateAccountName(String.valueOf(account.getId()), account.getName());
    }
}
