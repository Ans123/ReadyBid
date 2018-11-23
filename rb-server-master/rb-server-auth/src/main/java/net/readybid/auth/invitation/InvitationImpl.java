package net.readybid.auth.invitation;

import net.readybid.user.BasicUserDetails;
import net.readybid.user.BasicUserDetailsImpl;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 5/18/2017.
 *
 */
public class InvitationImpl extends BasicUserDetailsImpl implements Invitation {
    private String jobTitle;
    private String accountName;
    private ObjectId accountId;
    private String token;
    private ObjectId targetId;
    private Date expiryDate;
    private CreationDetails created;
    private InvitationStatusDetails status;

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public ObjectId getTargetId() {
        return targetId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountId(ObjectId accountId) {
        this.accountId = accountId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getAccountName() {
        return accountName;
    }

    public ObjectId getAccountId() {
        return accountId;
    }

    public void markAsUsed(BasicUserDetails user) {
        status = new InvitationStatusDetails(InvitationStatus.USED, user);
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setTargetId(ObjectId targetId) {
        this.targetId = targetId;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setStatus(InvitationStatusDetails status) {
        this.status = status;
    }

    public void setCreated(CreationDetails created) {
        this.created = created;
    }

    public InvitationStatusDetails getStatus() {
        return status;
    }

    public CreationDetails getCreated() {
        return created;
    }
}
