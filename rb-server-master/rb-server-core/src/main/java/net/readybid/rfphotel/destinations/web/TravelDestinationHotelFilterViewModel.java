package net.readybid.rfphotel.destinations.web;


import net.readybid.location.MaxDistanceTO;
import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.List;

/**
 * Created by DejanK on 1/23/2017.
 *
 */
public class TravelDestinationHotelFilterViewModel implements ViewModel<TravelDestinationHotelFilter> {

    public static final ViewModelFactory<TravelDestinationHotelFilter, TravelDestinationHotelFilterViewModel> FACTORY = TravelDestinationHotelFilterViewModel::new;


    public MaxDistanceTO maxDistance;
    public List<String> amenities;
    public List<String> chains;

    public TravelDestinationHotelFilterViewModel(TravelDestinationHotelFilter filter) {
        if(filter.maxDistance != null){
            maxDistance = new MaxDistanceTO();
            maxDistance.value = filter.maxDistance.getDistance();
            maxDistance.unit = filter.maxDistance.getDistanceUnit();
        }
        amenities = filter.amenities;
        chains = filter.chains;
    }
}
