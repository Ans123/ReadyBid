package net.readybid.app.interactors.rfp_hotel.bid.accepted_rates;

import net.readybid.app.entities.rfp_hotel.bid.HotelRfpAcceptedRates;
import net.readybid.entities.Id;
import net.readybid.user.BasicUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class HotelRfpAcceptedRatesFactoryImpl implements HotelRfpAcceptedRatesFactory {

    @Override
    public HotelRfpAcceptedRates create(List<String> validatedAcceptedRates, BasicUserDetails currentUser) {
        return new HotelRfpAcceptedRates.Builder()
                .setAcceptedRates(validatedAcceptedRates)
                .setBy(Id.valueOf(currentUser.getId()))
                .build();
    }
}
