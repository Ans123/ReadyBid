package net.readybid.rfphotel.destinations.web;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.location.LocationViewModel;
import net.readybid.mongodb.CreationDetailsViewModel;
import net.readybid.app.core.entities.traveldestination.TravelDestination;
import net.readybid.app.core.entities.traveldestination.TravelDestinationType;
import net.readybid.web.StatusDetailsViewModel;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public class TravelDestinationViewModel implements ViewModel<TravelDestination> {

    public static final ViewModelFactory<TravelDestination, TravelDestinationViewModel> FACTORY = TravelDestinationViewModel::new;

    public String id;
    public String rfpId;
    public TravelDestinationType type;
    public String name;
    public Long estimatedSpend;
    public Integer estimatedRoomNights;
    public LocationViewModel location;
    public TravelDestinationHotelFilterViewModel filter;
    public CreationDetailsViewModel created;
    public StatusDetailsViewModel status;

    public TravelDestinationViewModel(TravelDestination travelDestination) {
        if(travelDestination == null) throw new NotFoundException();
        id = travelDestination.getId();
        rfpId = travelDestination.getRfpId();
        type = travelDestination.getType();
        name = travelDestination.getName();
        estimatedSpend = travelDestination.getEstimatedSpend();
        estimatedRoomNights = travelDestination.getEstimatedRoomNights();
        location = LocationViewModel.FACTORY.createAsPartial(travelDestination.getLocation());
        filter = TravelDestinationHotelFilterViewModel.FACTORY.createAsPartial(travelDestination.getFilter());
        created = CreationDetailsViewModel.FACTORY.createAsPartial(travelDestination.getCreated());
        status = StatusDetailsViewModel.FACTORY.createAsPartial(travelDestination.getStatus());
    }

}
