package net.readybid.rfphotel.bid.core.negotiations;

import net.readybid.bidmanagerview.BidManagerViewSide;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 7/25/2017.
 *
 */
public class HotelRfpNegotiationsImpl implements HotelRfpNegotiations {

    private ObjectId _id;
    private NegotiationsConfig config;
    private NegotiationsParties parties;
    private List<HotelRfpNegotiation> communication;

    @Override
    public ObjectId getBidId() {
        return _id;
    }

    @Override
    public NegotiationsConfig getConfig() {
        return config;
    }

    @Override
    public NegotiationsParties getParties() {
        return parties;
    }

    @Override
    public List<? extends Negotiation> getCommunication() {
        return communication;
    }

    @Override
    public HotelRfpNegotiation getLastCommunication() {
        int indexOfLastCommunication = communication.size()-1;
        return indexOfLastCommunication < 0 ? null : communication.get(indexOfLastCommunication);
    }

    @Override
    public HotelRfpNegotiation getSecondLastCommunication() {
        int indexOfSecondLastCommunication = communication.size()-2;
        return indexOfSecondLastCommunication < 0 ? null : communication.get(indexOfSecondLastCommunication);
    }

    @Override
    public HotelRfpNegotiation getFirstCommunication() {
        return communication.isEmpty() ? null : communication.get(0);
    }

    @Override
    public HotelRfpNegotiation getLastSupplierCommunication() {
        for (int i = communication.size()-1; i >= 0; i--) {
            final HotelRfpNegotiation neg = communication.get(i);
            if(BidManagerViewSide.SUPPLIER.equals(neg.getFrom().getType()))
                return neg;
        }
        return null;
    }

    public void setId(ObjectId id) {
        this._id = id;
    }

    public void setConfig(HotelRfpNegotiationsConfig config) {
        this.config = config;
    }

    public void setParties(NegotiationsPartiesImpl parties) {
        this.parties = parties;
    }

    public void setCommunication(List<HotelRfpNegotiation> communication) {
        this.communication = communication;
    }
}
