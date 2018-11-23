package net.readybid.api.main.bid;

import net.readybid.auth.invitation.Invitation;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.rfp.contact.RfpContact;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public interface BidInvitationFactory {
    Invitation create(RfpContact supplierContact, ObjectId id, LocalDate dueDate, AuthenticatedUser currentUser);
}
