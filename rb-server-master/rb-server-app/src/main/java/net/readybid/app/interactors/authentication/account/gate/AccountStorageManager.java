package net.readybid.app.interactors.authentication.account.gate;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.auth.account.core.Account;

public interface AccountStorageManager {
    Account updateBasicInformationFromEntity(Entity entity);

    Account updateLogoFromEntity(Entity entity);
}
