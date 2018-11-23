package net.readybid.rfp.buyer;

import net.readybid.rfp.company.RfpCompany;
import net.readybid.rfp.contact.RfpContact;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public interface Buyer {
    RfpCompany getCompany();

    RfpContact getContact();

    String getCompanyName();

    ObjectId getCompanyAccountId();

    ObjectId getCompanyEntityId();
}
