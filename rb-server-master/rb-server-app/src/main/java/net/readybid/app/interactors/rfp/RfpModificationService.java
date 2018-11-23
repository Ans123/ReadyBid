package net.readybid.app.interactors.rfp;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.user.User;

public interface RfpModificationService {
    void updateEntityBasicInformation(Entity entity, Account account);

    void updateAccountLogo(Account account);

    void handleUserDetailsChange(User user);

    void handleUserEmailAddressChange(User user);

    void handleUserProfilePictureChange(User user);
}
