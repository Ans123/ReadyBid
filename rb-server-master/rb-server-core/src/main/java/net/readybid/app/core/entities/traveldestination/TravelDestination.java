package net.readybid.app.core.entities.traveldestination;

import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.app.core.entities.location.Location;
import net.readybid.utils.CreationDetails;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public interface TravelDestination {
    String getId();

    String getName();

    Long getEstimatedSpend();

    Integer getEstimatedRoomNights();

    Location getLocation();

    CreationDetails getCreated();

    TravelDestinationStatusDetails getStatus();

    String getRfpId();

    TravelDestinationHotelFilter getFilter();

    Address getAddress();

    TravelDestinationType getType();

    boolean hasCoordinates(Coordinates coordinates);

    String getFullAddress();

    Coordinates getCoordinates();
}
