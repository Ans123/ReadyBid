package net.readybid.app.core.service.traveldestination;

import net.readybid.app.core.action.traveldestination.CreateTravelDestinationRequest;
import net.readybid.app.core.entities.traveldestination.TravelDestination;

import java.util.List;

public interface CreateTravelDestinationService {

    TravelDestination create(CreateTravelDestinationRequest requestModel);

    List<String> createAll(String rfpId, List<CreateTravelDestinationRequest> createModel);
}
