package net.readybid.app.interactors.authentication.invitation;

import net.readybid.auth.account.core.Account;

public interface InvitationModificationService {
    void updateInvitationsWithAccount(Account account);
}
