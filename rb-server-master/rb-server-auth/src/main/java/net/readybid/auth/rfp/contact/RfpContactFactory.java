package net.readybid.auth.rfp.contact;

import net.readybid.auth.useraccount.UserAccount;
import net.readybid.rfp.contact.RfpContact;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
public interface RfpContactFactory {
    RfpContact createContact(UserAccount userAccount);
}
