package net.readybid.auth.user;

import net.readybid.auth.account.core.Account;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.user.BasicUserDetailsImpl;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by DejanK on 4/10/2017.
 *
 */
public class UserImpl extends BasicUserDetailsImpl implements User {

    protected Set<ObjectId> userAccounts;
    protected ObjectId currentUserAccountId;
    protected UserAccount currentUserAccount;

    protected CreationDetails created;
    protected UserStatusDetails status;
    protected Long lastChanged;
    protected List<String> tutorials;

    protected String profilePicture;

    public void setCreated(CreationDetails created) {
        this.created = created;
    }

    @Override
    public Account getAccount() {
        return null == currentUserAccount ? null : currentUserAccount.getAccount();
    }

    @Override
    public ObjectId getAccountId() {
        return currentUserAccount == null ? null : currentUserAccount.getAccountId();
    }

    @Override
    public EntityType getAccountType() {
        return currentUserAccount == null ? null : currentUserAccount.getAccountType();
    }

    @Override
    public CreationDetails getCreated() {
        return created;
    }

    public void setStatus(UserStatusDetails status) {
        this.status = status;
    }

    public UserStatusDetails getStatus() {
        return status;
    }

    @Override
    public UserStatus getStatusValue() {
        return status == null ? null : status.getValue();
    }

    public void setLastChangeTimestamp(Long timestamp) {
        this.lastChanged = timestamp;
    }

    @Override
    public Long getLastChangeTimestamp() {
        return lastChanged;
    }

    @Override
    public void markLastChange() {
        lastChanged = new Date().getTime();
    }

    @Override
    public Long getLatestChangeTimestamp() {
        return currentUserAccount == null ? lastChanged : Math.max(lastChanged, currentUserAccount.getLatestChangeTimestamp());
    }

    public void setUserAccounts(Set<ObjectId> userAccounts) {
        this.userAccounts = userAccounts;
    }

    @Override
    public Set<ObjectId> getUserAccounts() {
        return userAccounts;
    }

    public void setCurrentUserAccountId(ObjectId currentUserAccountId) {
        this.currentUserAccountId = currentUserAccountId;
    }

    public ObjectId getCurrentUserAccountId() {
        return currentUserAccountId;
    }

    @Override
    public boolean isActive() {
        return status.isActive();
    }

    @Override
    public List<String> getTutorials() {
        return tutorials;
    }

    @Override
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setCurrentUserAccount(UserAccount currentUserAccount) {
        this.currentUserAccount = currentUserAccount;
        setCurrentUserAccountId(currentUserAccount == null ? null : currentUserAccount.getId());
    }

    @Override
    public UserAccount getCurrentUserAccount() {
        return currentUserAccount;
    }

    @Override
    public boolean isPendingEmailAddressVerification() {
        return status != null && UserStatus.PENDING_EMAIL_VERIFICATION.equals(status.getValue());
    }

    public void setTutorials(List<String> tutorials) {
        this.tutorials = tutorials;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
