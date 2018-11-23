package net.readybid.rfphotel.supplier;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.rfp.contact.RfpContact;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/17/2017.
 *
 */
public class HotelRfpSupplierImpl implements HotelRfpSupplier {

    private RfpHotel hotel;
    private RfpContact contact;

    @Override
    public RfpHotel getCompany() {
        return hotel;
    }

    public void setCompany(RfpHotel hotel) {
        this.hotel = hotel;
    }

    public void setContact(RfpContact contact) {
        this.contact = contact;
    }

    @Override
    public String getCompanyName() {
        return hotel == null ? null : hotel.getName();
    }

    @Override
    public Address getCompanyAddress() {
        return hotel != null ? hotel.getAddress() : null;
    }

    @Override
    public ObjectId getContactCompanyAccountId() {
        return null == contact ? null : contact.getCompanyAccountId();
    }

    @Override
    public EntityType getContactType() {
        return null == contact ? null : contact.getCompanyType();
    }

    @Override
    public RfpContact getContact() {
        return contact;
    }

    @Override
    public ObjectId getCompanyAccountId() {
        return hotel == null ? null : hotel.getAccountId();
    }

    @Override
    public ObjectId getCompanyEntityId() {
        return hotel == null ? null : hotel.getEntityId();
    }
}
