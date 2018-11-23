package net.readybid.app.use_cases.rfp_hotel.travel_destination.implementation;

import net.readybid.app.core.entities.location.distance.DistanceUnit;
import net.readybid.app.interactors.rfp_hotel.gate.HotelRfpBidLoader;
import net.readybid.app.use_cases.rfp_hotel.travel_destination.ListTravelDestination;
import net.readybid.rfphotel.bid.core.HotelRfpBid;
import net.readybid.rfphotel.supplier.RfpHotel;
import net.readybid.web.RbListViewModel;
import net.readybid.web.RbViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListTravelDestinationImpl implements ListTravelDestination {

    private final HotelRfpBidLoader bidLoader;

    @Autowired
    public ListTravelDestinationImpl(HotelRfpBidLoader bidLoader) {
        this.bidLoader = bidLoader;
    }

    @Override
    public RbViewModel listProperties(String rfpId, String destinationId) {
        final List<HotelRfpBid> bids = bidLoader.listTravelDestinationProperties(rfpId, destinationId);
        final List<TravelDestinationPropertyView> views = bids.stream()
                .map(TravelDestinationPropertyView::new).collect(Collectors.toList());
        return new RbListViewModel<>(views);
    }

    class TravelDestinationPropertyView {

        public final String id;
        public final String name;
        public final String fullAddress;
        public final double distanceMi;
        public final double distanceKm;

        TravelDestinationPropertyView(HotelRfpBid bid){
            final RfpHotel hotel = bid.getSupplierCompany();
            id = String.valueOf(bid.getId());
            name = hotel.getName();
            fullAddress = hotel.getFullAddress();
            distanceMi = bid.getDistance(DistanceUnit.MI);
            distanceKm = bid.getDistance(DistanceUnit.KM);
        }
    }
}
