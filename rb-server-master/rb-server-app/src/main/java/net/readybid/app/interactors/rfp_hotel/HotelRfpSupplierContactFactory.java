package net.readybid.app.interactors.rfp_hotel;

import net.readybid.app.entities.rfp_hotel.HotelRfpSupplierContact;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.useraccount.UserAccount;

public interface HotelRfpSupplierContactFactory {
    HotelRfpSupplierContact create(CreateHotelRfpSupplierContactRequest request, Account account);

    HotelRfpSupplierContact create(UserAccount userAccount);
}
