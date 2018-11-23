package net.readybid.location;

import net.readybid.app.core.entities.location.LocationImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
public class LocationCityTO {

    @Valid
    @NotNull
    public AddressCityTO address;

    @Valid
    @NotNull
    public CoordinatesTO coordinates;

    public LocationImpl toEntity() {
        return new LocationImpl(address.toEntity(), coordinates.load());
    }
}
