package net.readybid.auth.rfp.company;

import net.readybid.auth.account.core.Account;
import net.readybid.rfp.company.RfpCompany;
import net.readybid.rfp.company.RfpCompanyImpl;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 4/3/2017.
 *
 */
@Service
public class RfpCompanyFactoryImpl implements RfpCompanyFactory {

    @Override
    public RfpCompany create(Account account) {
        if(account == null) return null;

        final RfpCompanyImpl c = new RfpCompanyImpl();

        c.setEntityId(account.getEntityId());
        c.setAccountId(account.getId());

        c.setName(account.getName());
        c.setType(account.getType());
        c.setIndustry(account.getIndustry());
        c.setLocation(account.getLocation());
        c.setWebsite(account.getWebsite());
        c.setEmailAddress(account.getEmailAddress());
        c.setPhone(account.getPhone());
        c.setLogo(account.getLogo());

        return c;
    }
}
