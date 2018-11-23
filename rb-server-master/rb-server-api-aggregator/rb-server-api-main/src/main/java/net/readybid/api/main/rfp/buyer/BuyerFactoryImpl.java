package net.readybid.api.main.rfp.buyer;

import net.readybid.auth.rfp.company.RfpCompanyFactory;
import net.readybid.auth.rfp.contact.RfpContactFactory;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.app.core.entities.entity.Entity;
import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.buyer.BuyerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
@Service
public class BuyerFactoryImpl implements BuyerFactory {

    private final RfpContactFactory contactFactory;
    private final RfpCompanyFactory companyFactory;

    @Autowired
    public BuyerFactoryImpl(RfpContactFactory contactFactory, RfpCompanyFactory companyFactory) {
        this.contactFactory = contactFactory;
        this.companyFactory = companyFactory;
    }

    @Override
    public Buyer createBuyer(AuthenticatedUser user, Entity entity) {
        final BuyerImpl b = new BuyerImpl();

        b.setCompany(companyFactory.create(user.getAccount()));
        b.setContact(contactFactory.createContact(user.getCurrentUserAccount()));
        return b;
    }
}
