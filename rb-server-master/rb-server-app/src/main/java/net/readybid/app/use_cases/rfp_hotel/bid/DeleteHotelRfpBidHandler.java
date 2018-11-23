package net.readybid.app.use_cases.rfp_hotel.bid;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.web.RbViewModel;

import java.util.List;

public interface DeleteHotelRfpBidHandler {
    RbViewModel deleteBids(List<String> bidsIds, AuthenticatedUser currentUser);
}
