package net.readybid.app.interactors.rfp_hotel.bid.accepted_rates;

import net.readybid.app.entities.rfp_hotel.bid.HotelRfpAcceptedRates;
import net.readybid.user.BasicUserDetails;

import java.util.List;

public interface HotelRfpAcceptedRatesFactory {
    HotelRfpAcceptedRates create(List<String> validatedAcceptedRates, BasicUserDetails currentUser);
}
