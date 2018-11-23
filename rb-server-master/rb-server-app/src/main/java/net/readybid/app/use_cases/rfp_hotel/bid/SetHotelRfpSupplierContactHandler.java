package net.readybid.app.use_cases.rfp_hotel.bid;

import net.readybid.app.interactors.rfp_hotel.CreateHotelRfpSupplierContactRequest;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.web.RbViewModel;

import java.util.List;

public interface SetHotelRfpSupplierContactHandler {
    RbViewModel set(List<String> bidsIds, String userAccountId, AuthenticatedUser currentUser);

    RbViewModel set(List<String> bidsIds, CreateHotelRfpSupplierContactRequest request, AuthenticatedUser currentUser);
}
