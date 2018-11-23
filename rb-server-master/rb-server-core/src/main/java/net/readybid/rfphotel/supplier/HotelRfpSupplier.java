package net.readybid.rfphotel.supplier;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.rfp.contact.RfpContact;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 4/9/2017.
 *
 */
public interface HotelRfpSupplier {
    RfpHotel getCompany();

    RfpContact getContact();

    ObjectId getCompanyAccountId();

    ObjectId getCompanyEntityId();

    void setContact(RfpContact contact);

    String getCompanyName();

    Address getCompanyAddress();

    ObjectId getContactCompanyAccountId();

    EntityType getContactType();
}
