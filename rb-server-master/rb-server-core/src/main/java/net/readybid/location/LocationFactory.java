package net.readybid.location;

import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.app.core.entities.location.Location;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public interface LocationFactory {
    Address createAddress(AddressTO address);

    Coordinates createCoordinates(CoordinatesTO coordinates);

    Location createLocation(LocationTO location);
}
