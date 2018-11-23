package net.readybid.location;

import net.readybid.app.core.entities.location.Location;
import net.readybid.app.core.entities.location.LocationImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
public class LocationTO {

    @Valid
    @NotNull
    public AddressTO address;

    @Valid
    @NotNull
    public CoordinatesTO coordinates;

    public Location load() {
        return new LocationImpl(address.load(), coordinates.load());
    }
}
