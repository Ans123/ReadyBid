package net.readybid.app.entities.rfp_hotel;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.entity_factories.LocationTestFactory;
import net.readybid.test_utils.RbRandom;

public class HotelRfpSupplierContactTestFactory {

    public static HotelRfpSupplierContact random(){
        final HotelRfpSupplierContact contact = new HotelRfpSupplierContact();
        contact.id = RbRandom.id();
        contact.firstName = RbRandom.name();
        contact.lastName = RbRandom.name();
        contact.emailAddress = RbRandom.emailAddress();
        contact.phone = RbRandom.phone();
        contact.profilePicture = RbRandom.alphanumeric(100, false);
        contact.jobTitle = RbRandom.alphanumeric(100, false);
        contact.company = HotelRfpContactAccountTestFactory.random();
        contact.isUser = RbRandom.bool();

        return contact;
    }

    private HotelRfpSupplierContactTestFactory(){}

    private static class HotelRfpContactAccountTestFactory{

        private HotelRfpContactAccountTestFactory(){}

        public static HotelRfpContactAccount random() {
            final HotelRfpContactAccount account = new HotelRfpContactAccount();
            account.accountId = RbRandom.id();
            account.entityId = RbRandom.id();
            account.type = RbRandom.randomEnum(EntityType.class);
            account.name = RbRandom.name();
            account.emailAddress = RbRandom.emailAddress();
            account.phone = RbRandom.alphanumeric(20,false);
            account.logo = RbRandom.alphanumeric(50, false);
            account.website = RbRandom.alphanumeric(50, false);
            account.location = LocationTestFactory.randomAll();
            return account;
        }
    }
}