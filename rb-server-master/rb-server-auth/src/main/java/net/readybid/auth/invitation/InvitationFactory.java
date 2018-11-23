package net.readybid.auth.invitation;

import net.readybid.rfp.contact.RfpContact;
import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by DejanK on 5/18/2017.
 *
 */
public interface InvitationFactory {
    Invitation createInvitation(RfpContact supplierContact, ObjectId targetId, Date expiryDate, BasicUserDetails currentUser);
}
