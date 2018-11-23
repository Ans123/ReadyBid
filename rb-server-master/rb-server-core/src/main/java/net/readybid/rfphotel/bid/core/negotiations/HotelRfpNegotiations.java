package net.readybid.rfphotel.bid.core.negotiations;

import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
public interface HotelRfpNegotiations {
    ObjectId getBidId();

    NegotiationsConfig getConfig();

    NegotiationsParties getParties();

    List<? extends Negotiation> getCommunication();

    HotelRfpNegotiation getLastCommunication();

    HotelRfpNegotiation getSecondLastCommunication();

    HotelRfpNegotiation getFirstCommunication();

    HotelRfpNegotiation getLastSupplierCommunication();
}
