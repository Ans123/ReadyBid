package net.readybid.rfp.buyer;

import net.readybid.rfp.company.RfpCompany;
import net.readybid.rfp.contact.RfpContact;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public class BuyerImpl implements Buyer{

    private RfpCompany company;

    private RfpContact contact;

    public void setCompany(RfpCompany company) {
        this.company = company;
    }

    public void setContact(RfpContact contact) {
        this.contact = contact;
    }

    @Override
    public RfpCompany getCompany() {
        return company;
    }

    @Override
    public RfpContact getContact() {
        return contact;
    }

    @Override
    public String getCompanyName() {
        return null == company ? "" : company.getName();
    }

    @Override
    public ObjectId getCompanyAccountId() {
        return null == company ? null : company.getAccountId();
    }

    @Override
    public ObjectId getCompanyEntityId() {
        return null == company ? null : company.getEntityId();
    }
}
