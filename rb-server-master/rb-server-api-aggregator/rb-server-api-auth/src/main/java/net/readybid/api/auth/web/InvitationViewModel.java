package net.readybid.api.auth.web;

import net.readybid.auth.invitation.Invitation;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

import java.util.Date;

public class InvitationViewModel implements ViewModel<Invitation> {

    public static final ViewModelFactory<Invitation, InvitationViewModel> FACTORY = InvitationViewModel::new;

    public ObjectId id;
    public String firstName;
    public String lastName;
    public String username;
    public String emailAddress;
    public String phone;
    public String jobTitle;
    public ObjectId accountId;
    public String accountName;
    public String token;
    public ObjectId targetId;
    public Date expiryDate;
    public boolean isUser;

    public InvitationViewModel(Invitation invitation) {
        this.id = invitation.getId();
        this.firstName = invitation.getFirstName();
        this.lastName = invitation.getLastName();
        this.username = invitation.getFullName();
        this.emailAddress = invitation.getEmailAddress();
        this.phone = invitation.getPhone();
        this.jobTitle = invitation.getJobTitle();
        this.accountId = invitation.getAccountId();
        this.accountName = invitation.getAccountName();
        this.token = invitation.getToken();
        this.targetId = invitation.getTargetId();
        this.expiryDate = invitation.getExpiryDate();
    }
}
