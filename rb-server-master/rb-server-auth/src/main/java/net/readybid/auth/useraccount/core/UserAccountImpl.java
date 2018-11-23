package net.readybid.auth.useraccount.core;

import net.readybid.auth.user.User;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.permission.Permission;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.UserAccountStatus;
import net.readybid.auth.useraccount.UserAccountStatusDetails;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DejanK on 3/27/2017.
 *
 */
public class UserAccountImpl implements UserAccount {

    private ObjectId id;
    private ObjectId userId;
    private Account account;
    private User user;
    private String jobTitle;
    private CreationDetails created;
    private UserAccountStatusDetails status;
    private long changed;
    private ObjectId defaultBidManagerView;
    private ObjectId lastBidManagerView;

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setCreated(CreationDetails created) {
        this.created = created;
    }

    public CreationDetails getCreated() {
        return created;
    }

    @Override
    public UserAccountStatusDetails getStatus() {
        return status;
    }

    public void setStatus(UserAccountStatusDetails status) {
        this.status = status;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public String getFirstName() {
        return null == user ? null : user.getFirstName();
    }

    @Override
    public String getLastName() {
        return null == user ? null : user.getLastName();
    }

    @Override
    public String getFullName() {
        return null == user ? null : user.getFullName();
    }

    @Override
    public String getEmailAddress() {
        return null == user ? null : user.getEmailAddress();
    }

    public ObjectId getUserId() {
        return userId;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    public ObjectId getAccountId() {
        return account == null ? null : account.getId();
    }

    @Override
    public boolean hasPermission(ObjectId accountId, Permission permission) {
        return account != null && account.hasPermission(accountId, permission);
    }

    @Override
    public void setDefaultView(ObjectId id) {
        defaultBidManagerView = id;
    }

    @Override
    public void setLastUsedBidManager(ObjectId viewId) {
        lastBidManagerView = viewId;
    }

    @Override
    public ObjectId getLastUsedBidManagerView() {
        return lastBidManagerView;
    }

    @Override
    public boolean shouldUpdateLastBidManagerView(ObjectId objectViewId) {
        return !objectViewId.equals(lastBidManagerView);
    }

    @Override
    public List<ObjectId> getAccountIdsWithPermission(Permission permission) {
        return account == null ? new ArrayList<>() : account.getAccountIdsWithPermission(permission);
    }

    @Override
    public boolean isActive() {
        return status.isActive();
    }

    @Override
    public String getAccountName() {
        return account == null ? null : account.getName();
    }

    @Override
    public ObjectId getEntityId() {
        return null == account ? null : account.getEntityId();
    }

    @Override
    public EntityType getAccountType() {
        return account == null ? null : account.getType();
    }

    @Override
    public boolean isUserActive() {
        if(user == null) throw new UnableToCompleteRequestException("user is null");
        return user.isActive();
    }

    @Override
    public String getAccountOrUserEmailAddress() {
        final String emailAddress = account == null ? null : account.getEmailAddress();
        return emailAddress == null || emailAddress.isEmpty() ? getEmailAddress() : emailAddress;
    }

    @Override
    public String getProfilePicture() {
        return null == user ? null : user.getProfilePicture();
    }

    @Override
    public String getPhone() {
        return null == user ? null : user.getPhone();
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public UserAccountStatusDetails getUserAccountStatus() {
        return status;
    }

    @Override
    public Long getLastChangeTimestamp() {
        return changed;
    }

    @Override
    public Long getLatestChangeTimestamp() {
        return account == null ? changed : Math.max(changed, account.getLastChangeTimestamp());
    }

    @Override
    public UserAccountStatus getStatusValue() {
        return null == status ? null : status.getValue();
    }

    @Override
    public void markLastChange() {
        changed = new Date().getTime();
    }

    @Override
    public void setDefaultBidManagerView(ObjectId defaultBidManagerViewId) {
        defaultBidManagerView = defaultBidManagerViewId;
    }

    public void setChanged(Long changed) {
        this.changed = changed;
    }

    public ObjectId getDefaultBidManagerView() {
        return defaultBidManagerView;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user == null ? null : user.getId();
    }

    @Override
    public InternetAddress getInternetAddress() throws UnsupportedEncodingException {
        return null == user ? null : user.getInternetAddress();
    }
}
