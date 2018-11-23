package net.readybid.app.interactors.rfp_hotel.bid.implementation;

import net.readybid.app.interactors.rfp_hotel.bid.HotelRfpBidStateFactory;
import net.readybid.rfphotel.bid.core.*;
import net.readybid.user.BasicUserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HotelRfpBidStateFactoryImpl implements HotelRfpBidStateFactory {

    @Override
    public HotelRfpBidState createSimpleState(HotelRfpBidStateStatus status, BasicUserDetails by, Date at) {
        final HotelRfpBidSimpleState state = new HotelRfpBidSimpleState();
        state.setStatus(status);
        state.setBy(by);
        state.setAt(at);
        return state;
    }

    @Override
    public HotelRfpBidState createSimpleState(HotelRfpBidStateStatus status, BasicUserDetails by) {
        return createSimpleState(status, by, new Date());
    }

    @Override
    public HotelRfpBidReceivedState createReceivedState(HotelRfpBidStatusDetails buyerStatusDetails, long errorsCount, boolean isTouched, Date at, BasicUserDetails currentUser) {
        final HotelRfpBidReceivedState state = new HotelRfpBidReceivedState();

        state.setSupplierBy(currentUser);
        state.setSupplierAt(at);
        state.setBuyerBy(buyerStatusDetails == null ? null : buyerStatusDetails.by);
        state.setBuyerAt(buyerStatusDetails == null ? null : buyerStatusDetails.at);
        state.setErrorsCount(errorsCount);
        state.setResponseTouched(isTouched);

        return state;
    }

    @Override
    public HotelRfpBidState createReceivedState(HotelRfpBidStatusDetails buyerStatusDetails, long errorsCount, boolean isTouched, BasicUserDetails currentUser) {
        return createReceivedState(buyerStatusDetails, errorsCount, isTouched, new Date(), currentUser);
    }
}
