package net.readybid.app.interactors.rfp_hotel.implementation;

import net.readybid.app.entities.rfp_hotel.HotelRfpContactAccount;
import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.app.interactors.core.gate.IdFactory;
import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.app.interactors.rfp_hotel.HotelRfpSupplierContactFactory;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.entities.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class HotelRfpSupplierContactFactoryImpl implements HotelRfpSupplierContactFactory {

    private final IdFactory idFactory;
    private final HotelRfpSupplierAccountFactory hotelRfpSupplierAccountFactory;

    @Autowired
    HotelRfpSupplierContactFactoryImpl(IdFactory idFactory) {
        this.idFactory = idFactory;
        hotelRfpSupplierAccountFactory = new HotelRfpSupplierAccountFactory();
    }

    @Override
    public HotelRfpSupplierContact create(CreateHotelRfpSupplierContactRequest request, Account account) {
        final HotelRfpSupplierContact supplierContact = new HotelRfpSupplierContact();

        supplierContact.isUser = false;
        supplierContact.id = idFactory.createId();
        supplierContact.firstName = request.firstName;
        supplierContact.lastName = request.lastName;
        supplierContact.emailAddress = request.emailAddress;
        supplierContact.phone = request.phone;
        supplierContact.jobTitle = request.jobTitle;
        supplierContact.company = hotelRfpSupplierAccountFactory.create(account);

        return supplierContact;
    }

    @Override
    public HotelRfpSupplierContact create(UserAccount userAccount) {
        final HotelRfpSupplierContact supplierContact = new HotelRfpSupplierContact();

        supplierContact.isUser = true;
        supplierContact.id = Id.valueOf(userAccount.getId());
        supplierContact.firstName = userAccount.getFirstName();
        supplierContact.lastName = userAccount.getLastName();
        supplierContact.emailAddress = userAccount.getEmailAddress();
        supplierContact.phone = userAccount.getPhone();
        supplierContact.profilePicture = userAccount.getProfilePicture();
        supplierContact.jobTitle = userAccount.getJobTitle();
        supplierContact.company = hotelRfpSupplierAccountFactory.create(userAccount.getAccount());

        return supplierContact;
    }

    private class HotelRfpSupplierAccountFactory {

        public HotelRfpContactAccount create(Account account) {
            final HotelRfpContactAccount contactAccount = new HotelRfpContactAccount();
            contactAccount.accountId = Id.valueOf(account.getId());
            contactAccount.entityId = Id.valueOf(account.getEntityId());
            contactAccount.type = account.getType();
            contactAccount.name = account.getName();
            contactAccount.website = account.getWebsite();
            contactAccount.emailAddress = account.getEmailAddress();
            contactAccount.phone = account.getPhone();
            contactAccount.logo = account.getLogo();
            contactAccount.location = account.getLocation();
            return contactAccount;
        }
    }
}