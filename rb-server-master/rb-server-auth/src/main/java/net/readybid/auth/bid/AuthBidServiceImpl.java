package net.readybid.auth.bid;

import net.readybid.auth.rfp.contact.RfpContactFactory;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.rfp.contact.RfpContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 5/21/2017.
 *
 */
@Service
public class AuthBidServiceImpl implements AuthBidService {

    private final RfpContactFactory rfpContactFactory;
    private final AuthBidRepository authBidRepository;

    @Autowired
    public AuthBidServiceImpl(
            RfpContactFactory rfpContactFactory,
            AuthBidRepository authBidRepository
    ) {
        this.rfpContactFactory = rfpContactFactory;
        this.authBidRepository = authBidRepository;
    }

    @Override
    public void onNewUserAccount(UserAccount userAccount) {
        final RfpContact contact = rfpContactFactory.createContact(userAccount);
        authBidRepository.setNewSupplierContactToAllAccountBidsWithoutSupplierContact(contact, userAccount.getAccountId());
    }
}
