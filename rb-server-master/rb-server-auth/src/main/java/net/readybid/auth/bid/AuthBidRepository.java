package net.readybid.auth.bid;

import net.readybid.rfp.contact.RfpContact;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 5/21/2017.
 *
 */
public interface AuthBidRepository {

    void setNewSupplierContactToAllAccountBidsWithoutSupplierContact(RfpContact contact, ObjectId accountId);
}
