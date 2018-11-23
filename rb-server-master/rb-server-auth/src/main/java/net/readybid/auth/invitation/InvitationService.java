package net.readybid.auth.invitation;

import net.readybid.rfp.contact.RfpContact;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by DejanK on 5/18/2017.
 *
 */
public interface InvitationService {
    Invitation getInvitation(String token);

    Invitation newInvitation(RfpContact supplierContact, ObjectId targetId, Date expiryDate, BasicUserDetails currentUser);

    Invitation newInvitation(RfpContact supplierContact, String id, Date expiryDate, BasicUserDetails currentUser);
}
