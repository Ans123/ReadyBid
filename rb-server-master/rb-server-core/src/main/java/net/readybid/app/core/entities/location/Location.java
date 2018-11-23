package net.readybid.app.core.entities.location;

import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.coordinates.Coordinates;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public interface Location {

    Address getAddress();

    void setAddress(Address address);

    String getFullAddress();

    void setCoordinates(Coordinates coordinates);

    Coordinates getCoordinates();

    boolean hasCoordinates(Coordinates coordinates);
}
