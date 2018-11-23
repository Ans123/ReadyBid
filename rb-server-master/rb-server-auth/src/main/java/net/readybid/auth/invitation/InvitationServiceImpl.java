package net.readybid.auth.invitation;

import net.readybid.rfp.contact.RfpContact;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by DejanK on 5/18/2017.
 *
 */
@Service
public class InvitationServiceImpl implements InvitationService {

    private final InvitationTokenService invitationTokenService;
    private final InvitationFactory invitationFactory;
    private final InvitationRepository repository;

    @Autowired
    public InvitationServiceImpl(
            InvitationTokenService invitationTokenService,
            InvitationFactory invitationFactory,
            InvitationRepository repository
    ) {
        this.invitationTokenService = invitationTokenService;
        this.invitationFactory = invitationFactory;
        this.repository = repository;
    }

    @Override
    public Invitation getInvitation(String token) {
        final Map<String, Object> payload = invitationTokenService.parseToken(token);
        return payload == null ? null : repository.getActiveInvitationById(String.valueOf(payload.get("id")));
    }

    @Override
    public Invitation newInvitation(RfpContact supplierContact, ObjectId targetId, Date expiryDate, BasicUserDetails currentUser) {
        final Invitation invitation = invitationFactory.createInvitation(supplierContact, targetId, expiryDate, currentUser);
        repository.create(invitation);
        return invitation;
    }

    @Override
    public Invitation newInvitation(RfpContact supplierContact, String id, Date expiryDate, BasicUserDetails currentUser) {
        return newInvitation(supplierContact, new ObjectId(id), expiryDate, currentUser);
    }
}
