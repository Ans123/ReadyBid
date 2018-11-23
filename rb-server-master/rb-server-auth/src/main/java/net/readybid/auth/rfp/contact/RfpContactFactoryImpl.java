package net.readybid.auth.rfp.contact;

import net.readybid.auth.rfp.company.RfpCompanyFactory;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.rfp.contact.RfpContact;
import net.readybid.rfp.contact.RfpContactImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
@Service
public class RfpContactFactoryImpl implements RfpContactFactory {

    private final RfpCompanyFactory rfpCompanyFactory;

    @Autowired
    public RfpContactFactoryImpl(RfpCompanyFactory rfpCompanyFactory) {
        this.rfpCompanyFactory = rfpCompanyFactory;
    }

    @Override
    public RfpContact createContact(UserAccount userAccount) {
        final RfpContactImpl c = new RfpContactImpl();

        c.setId(userAccount.getId());
        c.setFirstName(userAccount.getFirstName());
        c.setLastName(userAccount.getLastName());
        c.setFullName(userAccount.getFullName());
        c.setEmailAddress(userAccount.getEmailAddress().toLowerCase());
        c.setPhone(userAccount.getPhone());
        c.setJobTitle(userAccount.getJobTitle());
        c.setCompany(rfpCompanyFactory.create(userAccount.getAccount()));
        c.setIsUser(true);

        return c;
    }
}
