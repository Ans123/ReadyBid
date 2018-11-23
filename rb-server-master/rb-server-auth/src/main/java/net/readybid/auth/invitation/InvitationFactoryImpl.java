package net.readybid.auth.invitation;

import net.readybid.rfp.contact.RfpContact;
import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by DejanK on 5/18/2017.
 */
@Service
public class InvitationFactoryImpl implements InvitationFactory {

    private final InvitationTokenService invitationTokenService;

    @Autowired
    public InvitationFactoryImpl(InvitationTokenService invitationTokenService) {
        this.invitationTokenService = invitationTokenService;
    }

    @Override
    public Invitation createInvitation(RfpContact supplierContact, ObjectId targetId, Date expiryDate, BasicUserDetails currentUser) {
        final InvitationImpl invitation = new InvitationImpl();
        invitation.setId(new ObjectId());
        invitation.setFirstName(supplierContact.getFirstName());
        invitation.setLastName(supplierContact.getLastName());
        invitation.setEmailAddress(supplierContact.getEmailAddress());
        invitation.setPhone(supplierContact.getPhone());
        invitation.setJobTitle(supplierContact.getJobTitle());
        invitation.setAccountName(supplierContact.getCompanyName());
        invitation.setAccountId(supplierContact.getCompanyAccountId());
        invitation.setTargetId(targetId);
        invitation.setExpiryDate(expiryDate);
        invitation.setToken(invitationTokenService.createToken(invitation));
        invitation.setCreated(new CreationDetails(currentUser));
        invitation.setStatus(new InvitationStatusDetails(invitation.getCreated()));
        return invitation;
    }
}
