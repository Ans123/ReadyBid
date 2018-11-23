package net.readybid.auth.useraccount;

import net.readybid.auth.account.core.Account;
import net.readybid.auth.permission.Permission;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.util.List;


/**
 * Created by DejanK on 3/24/2017.
 *
 */
public interface UserAccount extends BasicUserDetails {

    ObjectId getUserId();

    Account getAccount();

    String getJobTitle();

    CreationDetails getCreated();

    UserAccountStatusDetails getStatus();

    /**
     * @return last change timestamp from UserAccount
     */
    Long getLastChangeTimestamp();

    /**
     * @return max last change timestamp from UserAccount and Account
     */
    Long getLatestChangeTimestamp();

    UserAccountStatus getStatusValue();

    void markLastChange();

    void setDefaultBidManagerView(ObjectId id);

    ObjectId getDefaultBidManagerView();

    ObjectId getAccountId();

    boolean hasPermission(ObjectId accountId, Permission permission);

    void setDefaultView(ObjectId id);

    void setLastUsedBidManager(ObjectId viewId);

    ObjectId getLastUsedBidManagerView();

    boolean shouldUpdateLastBidManagerView(ObjectId objectViewId);

    List<ObjectId> getAccountIdsWithPermission(Permission permission);

    boolean isActive();

    String getAccountName();

    ObjectId getEntityId();

    EntityType getAccountType();

    boolean isUserActive();

    String getAccountOrUserEmailAddress();

    String getProfilePicture();
}
