package net.readybid.app.core.service.traveldestination;

import net.readybid.app.core.entities.traveldestination.TravelDestinationHotelFilter;
import net.readybid.app.core.entities.traveldestination.TravelDestinationImpl;
import net.readybid.app.core.entities.traveldestination.TravelDestinationStatusDetails;

import java.util.List;

public interface SaveTravelDestinationRepository {
    void create(TravelDestinationImpl destination);

    void save(TravelDestinationImpl destination);

    void markAsDeleted(String destinationId, TravelDestinationStatusDetails deletedStatus);

    void setFilter(String destinationId, TravelDestinationHotelFilter filter);

    void updateRfpTravelDestinationFilters(String rfpId, TravelDestinationHotelFilter filter);

    void createAll(List<? extends TravelDestinationImpl> travelDestinations);

}
