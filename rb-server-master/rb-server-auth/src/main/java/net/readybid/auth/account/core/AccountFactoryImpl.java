package net.readybid.auth.account.core;

import net.readybid.auth.permission.Permission;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
@Service
public class AccountFactoryImpl implements AccountFactory {

    @Override
    /**
     * NOTICE!!!
     *
     * Creator may be a User that is NOT Account Representative!!
     * This happens when Buyer is creating a Bid and Account for Supplier doesn't exist
     */
    public Account createAccount(Entity entity, BasicUserDetails currentUser) {
        final AccountImpl account = new AccountImpl();

        account.setId(new ObjectId());
        account.setType(entity.getType());
        account.setEntityId(entity.getId());
        account.setName(entity.getName());
        account.setIndustry(entity.getIndustry());
        account.setLocation(entity.getLocation());
        account.setWebsite(entity.getWebsite());
        account.setLogo(entity.getLogo());
        account.setPhone(entity.getPhone());
        account.setEmailAddress(entity.getEmailAddress());
        account.replacePermissionSet(String.valueOf(account.getId()), Permission.allowAll());

        account.setCreated(new CreationDetails(currentUser));
        account.setStatus(new AccountStatusDetails(account.getCreated(), AccountStatus.UNVERIFIED));
        account.markLastChange();

        return account;
    }
}
