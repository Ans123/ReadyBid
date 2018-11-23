package net.readybid.app.interactors.rfp_hotel.bid.negotiation;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.bidmanagerview.BidManagerViewSide;
import net.readybid.rfp.type.RfpType;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiation;
import net.readybid.rfphotel.bid.core.negotiations.HotelRfpNegotiationsConfig;
import net.readybid.rfphotel.bid.core.negotiations.NegotiationRequest;
import net.readybid.rfphotel.bid.core.negotiations.NegotiatorImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by DejanK on 7/27/2017.
 *
 */
@Service
public class HotelRfpNegotiationFactoryImpl implements HotelRfpNegotiationFactory {

    private final HotelRfpNegotiationValuesFromBidResponseFactory fromBidResponseFactory;
    private final HotelRfpNegotiationValuesFromNegotiationResponseFactory fromNegotiationResponseFactory;

    @Autowired
    public HotelRfpNegotiationFactoryImpl(
            HotelRfpNegotiationValuesFromBidResponseFactory fromBidResponseFactory,
            HotelRfpNegotiationValuesFromNegotiationResponseFactory fromNegotiationResponseFactory
    ) {
        this.fromBidResponseFactory = fromBidResponseFactory;
        this.fromNegotiationResponseFactory = fromNegotiationResponseFactory;
    }

    @Override
    public HotelRfpNegotiation create(HotelRfpNegotiationsConfig config, Map<String, String> response, AuthenticatedUser currentUser) {
        final HotelRfpNegotiation negotiation = new HotelRfpNegotiation();
        negotiation.setId(new ObjectId());
        negotiation.setAt(new Date());
        negotiation.setFrom(createNegotiator(currentUser));
        negotiation.setMessage("");
        negotiation.setValues(fromBidResponseFactory.readValues(config, response));
        return negotiation;
    }

    @Override
    public HotelRfpNegotiation create(NegotiationRequest negotiationRequest, HotelRfpNegotiation lastCommunication, AuthenticatedUser currentUser) {
        return createNegotiation(new ObjectId(), negotiationRequest, lastCommunication, currentUser);
    }

    @Override
    public HotelRfpNegotiation create(String negotiationId, NegotiationRequest negotiationRequest, HotelRfpNegotiation previousCommunication, AuthenticatedUser currentUser) {
        if(!ObjectId.isValid(negotiationId)) throw new NotFoundException();
        return createNegotiation(new ObjectId(negotiationId), negotiationRequest, previousCommunication, currentUser);
    }

    private HotelRfpNegotiation createNegotiation(ObjectId negotiationId, NegotiationRequest negotiationRequest, HotelRfpNegotiation previousCommunication, AuthenticatedUser currentUser) {
        final HotelRfpNegotiation negotiation = new HotelRfpNegotiation();
        negotiation.setId(negotiationId);
        negotiation.setAt(new Date());
        negotiation.setFrom(createNegotiator(currentUser));
        negotiation.setMessage(negotiationRequest.message == null ? "" : negotiationRequest.message);
        negotiation.setValues(fromNegotiationResponseFactory.readValues(negotiationRequest, previousCommunication.getValues()));
        return negotiation;
    }

    private NegotiatorImpl createNegotiator(AuthenticatedUser currentUser) {
        return new NegotiatorImpl(currentUser.getCurrentUserAccountId(), BidManagerViewSide.determineSide(RfpType.HOTEL, currentUser.getAccount().getType()));
    }
}
