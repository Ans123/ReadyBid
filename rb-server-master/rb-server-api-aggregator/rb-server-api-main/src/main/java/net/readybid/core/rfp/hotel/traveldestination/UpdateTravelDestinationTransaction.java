package net.readybid.core.rfp.hotel.traveldestination;

import net.readybid.app.core.entities.traveldestination.TravelDestination;

public interface UpdateTravelDestinationTransaction {

    TravelDestination update(UpdateTravelDestinationRequest model);

    void updateFilter(UpdateTravelDestinationFilterRequest model);
}
