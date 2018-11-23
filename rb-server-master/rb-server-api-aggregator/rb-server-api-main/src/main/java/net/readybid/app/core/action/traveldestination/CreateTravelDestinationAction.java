package net.readybid.app.core.action.traveldestination;

import net.readybid.app.core.entities.traveldestination.*;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationFilterRequest;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationRequest;
import net.readybid.user.BasicUserDetails;

import java.util.List;

/**
 * Created by DejanK on 4/4/2017.
 *
 */
public interface CreateTravelDestinationAction {

    TravelDestinationImpl create(CreateTravelDestinationRequest requestModel, TravelDestinationHotelFilter rfpDefaultFilter);

    List<TravelDestinationImpl> createAll(List<CreateTravelDestinationRequest> createTravelDestinationRequests, TravelDestinationHotelFilter rfpDefaultFilter);

    void update(TravelDestination destination, UpdateTravelDestinationRequest requestModel);

    TravelDestinationStatusDetails createStatusDetails(TravelDestinationStatus status, BasicUserDetails currentUser);

    TravelDestinationHotelFilter createFilter(UpdateTravelDestinationFilterRequest model);
}
