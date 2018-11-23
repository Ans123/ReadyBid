package net.readybid.app.core.service.traveldestination;

import net.readybid.app.core.entities.traveldestination.TravelDestinationImpl;

import java.util.List;

public interface LoadTravelDestinationRepository {
    TravelDestinationImpl getById(String destinationId);

    List<TravelDestinationImpl> listByRfpId(String rfpId);
}
