package net.readybid.app.use_cases.rfp_hotel.bid;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.web.RbViewModel;

import java.util.List;

public interface HotelRfpAcceptRatesHandler {
    RbViewModel acceptRates(String bidId, List<String> validatedAcceptedRates, AuthenticatedUser currentUser);
}
