package net.readybid.app.use_cases.rfp_hotel.bid;

import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.web.RbViewModel;

import java.util.List;

public interface SendHotelRfpBidHandler {
    RbViewModel send(List<String> bidsIds, AuthenticatedUser currentUser, boolean ignoreDueDate);

    RbViewModel sendToNewContact(List<String> bidsIds, CreateHotelRfpSupplierContactRequest supplierContactRequest, AuthenticatedUser currentUser, boolean ignoreDueDate);

    RbViewModel sendToSelectedContact(List<String> bidsIds, String userAccountId, AuthenticatedUser currentUser, boolean ignoreDueDate);
}
