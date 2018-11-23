package net.readybid.app.use_cases.rfp_hotel.bid.implementation;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.app.entities.rfp_hotel.bid.HotelRfpAcceptedRates;
import net.readybid.app.interactors.rfp_hotel.bid.accepted_rates.HotelRfpAcceptedRatesFactory;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidStorageManager;
import net.readybid.app.use_cases.rfp_hotel.bid.HotelRfpAcceptRatesHandler;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.exceptions.UnableToCompleteRequestException;
import net.readybid.web.RbSuccessViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelRfpAcceptRatesHandlerImpl implements HotelRfpAcceptRatesHandler {

    private final HotelRfpBidStorageManager bidStorageManager;
    private final HotelRfpAcceptedRatesFactory hotelRfpAcceptedRatesFactory;

    public HotelRfpAcceptRatesHandlerImpl(
            HotelRfpBidStorageManager bidStorageManager,
            HotelRfpAcceptedRatesFactory hotelRfpAcceptedRatesFactory
    ) {
        this.bidStorageManager = bidStorageManager;
        this.hotelRfpAcceptedRatesFactory = hotelRfpAcceptedRatesFactory;
    }

    @Override
    public RbViewModel acceptRates(String bidId, List<String> validatedAcceptedRates, AuthenticatedUser currentUser) {
        final HotelRfpAcceptedRates hotelRfpAcceptedRates = hotelRfpAcceptedRatesFactory.create(validatedAcceptedRates, currentUser);
        try {
            bidStorageManager.setAcceptedRates(bidId, hotelRfpAcceptedRates);
            return new RbSuccessViewModel();
        } catch (NotFoundException nfe){
            throw new UnableToCompleteRequestException("BID_STATE_CHANGED");
        }
    }
}
