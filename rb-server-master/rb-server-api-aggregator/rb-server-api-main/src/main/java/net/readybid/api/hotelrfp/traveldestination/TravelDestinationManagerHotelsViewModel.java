package net.readybid.api.hotelrfp.traveldestination;

import net.readybid.rfphotel.bid.core.HotelRfpBidSupplierCompanyEntityAndSubject;
import net.readybid.web.RbViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravelDestinationManagerHotelsViewModel implements RbViewModel {

    private List<TravelDestinationManagerHotelViewModel> destinationsViewModels;

    TravelDestinationManagerHotelsViewModel(List<TravelDestinationManagerHotelViewModel> hotels, List<HotelRfpBidSupplierCompanyEntityAndSubject> bids, String destinationId, boolean isDevelopmentEnvironment) {
        super();

        destinationsViewModels = hotels;
        final Map<String, TravelDestinationManagerHotelViewModel> viewMap = new HashMap<>();
        for (TravelDestinationManagerHotelViewModel hotel : hotels) {
            viewMap.put(hotel.id, hotel);
            if(isDevelopmentEnvironment){
                hotel.setLastYear( hotel.chain != null && hotel.chain.masterChain != null
                        && hotel.chain.masterChain.code.equals("EH"));
            }
        }

        for(HotelRfpBidSupplierCompanyEntityAndSubject bid : bids){
            final TravelDestinationManagerHotelViewModel view = viewMap.get(bid.getSupplierCompanyEntityId().toString());
            if(view != null){
                if(destinationId.equals(String.valueOf(bid.getSubjectId()))){
                    view.setBid(bid);
                }
            }
        }
    }

    @Override
    public Long getCount() {
        return (long) destinationsViewModels.size();
    }

    @Override
    public Object getData() {
        return destinationsViewModels;
    }
}
