package net.readybid.api.hotelrfp.traveldestination;

import net.readybid.entities.chain.HotelChainListItemViewModel;
import net.readybid.entities.core.EntityView;
import net.readybid.rfphotel.bid.core.HotelRfpBidStateStatus;
import net.readybid.rfphotel.bid.core.HotelRfpBidSupplierCompanyEntityAndSubject;
import org.bson.types.ObjectId;

import java.util.List;

public class TravelDestinationManagerHotelViewModel extends EntityView {

    public HotelChainListItemViewModel chain;
    public List<String> amenities;
    public int rating;
    public double distanceMi;
    public double distanceKm;

    public ObjectId bidId;
    public String bidState = "NA";
    public boolean lastYear = false;

    public void setBid(HotelRfpBidSupplierCompanyEntityAndSubject bid) {
        bidId = bid.getId();
        bidState = getBidState(bid.getStateStatus());
    }

    private String getBidState(HotelRfpBidStateStatus status) {
        return status.equals(HotelRfpBidStateStatus.CREATED) ? "ADDED" : "ACTIVE";
    }

    public void setLastYear(boolean lastYear) {
        this.lastYear = lastYear;
    }
}
