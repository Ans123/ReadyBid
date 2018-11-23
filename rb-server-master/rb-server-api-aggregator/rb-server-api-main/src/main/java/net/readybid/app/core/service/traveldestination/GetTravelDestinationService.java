package net.readybid.app.core.service.traveldestination;

import net.readybid.app.core.entities.traveldestination.TravelDestination;

public interface GetTravelDestinationService {
    TravelDestination get(String rfpId, String destinationId);

    ListTravelDestinationsResult listRfpDestinations(String rfpId);
}
