package net.readybid.auth.user;

import net.readybid.auth.account.core.Account;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Set;

/**
 * Created by DejanK on 4/10/2017.
 *
 */
public interface User extends BasicUserDetails {

    Account getAccount();

    ObjectId getAccountId();

    EntityType getAccountType();

    void setStatus(UserStatusDetails userStatus);

    UserStatusDetails getStatus();

    UserStatus getStatusValue();

    boolean isPendingEmailAddressVerification();

    CreationDetails getCreated();

    void markLastChange();

    /**
     * @return last change timestamp from AuthenticatedUser
     */
    Long getLastChangeTimestamp();

    /**
     * @return max last change timestamp from AuthenticatedUser and UserAccount
     */
    Long getLatestChangeTimestamp();

    Set<ObjectId> getUserAccounts();

    UserAccount getCurrentUserAccount();

    ObjectId getCurrentUserAccountId();

    boolean isActive();

    List<String> getTutorials();

    String getProfilePicture();
}
