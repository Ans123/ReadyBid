package net.readybid.api.main.bid;

import net.readybid.auth.invitation.Invitation;
import net.readybid.auth.invitation.InvitationImpl;
import net.readybid.auth.invitation.InvitationStatusDetails;
import net.readybid.auth.invitation.InvitationTokenService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class BidInvitationFactoryImpl implements BidInvitationFactory {

    private final InvitationTokenService invitationTokenService;

    @Autowired
    public BidInvitationFactoryImpl(InvitationTokenService invitationTokenService) {
        this.invitationTokenService = invitationTokenService;
    }

    @Override
    public Invitation create(RfpContact supplierContact, ObjectId bidId, LocalDate bidDueDate, AuthenticatedUser currentUser) {
        final InvitationImpl invitation = new InvitationImpl();
        invitation.setId(new ObjectId());
        invitation.setFirstName(supplierContact.getFirstName());
        invitation.setLastName(supplierContact.getLastName());
        invitation.setEmailAddress(supplierContact.getEmailAddress());
        invitation.setPhone(supplierContact.getPhone());
        invitation.setJobTitle(supplierContact.getJobTitle());
        invitation.setAccountName(supplierContact.getCompanyName());
        invitation.setAccountId(supplierContact.getCompanyAccountId());
        invitation.setTargetId(bidId);
        invitation.setExpiryDate(Date.from(bidDueDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        invitation.setToken(invitationTokenService.createToken(invitation));
        invitation.setCreated(new CreationDetails(currentUser));
        invitation.setStatus(new InvitationStatusDetails(invitation.getCreated()));
        return invitation;

    }
}
