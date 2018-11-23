package net.readybid.auth.rfp.company;

import net.readybid.auth.account.core.Account;
import net.readybid.rfp.company.RfpCompany;

/**
 * Created by DejanK on 4/3/2017.
 *
 */
public interface RfpCompanyFactory {

    RfpCompany create(Account account);
}
