package net.readybid.app.gate.api.rfps.traveldestinations.create;

import net.readybid.app.core.entities.location.Location;
import net.readybid.location.LocationCityTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class CreateTravelDestinationCityRequest extends CreateTravelDestinationWebRequest {

    @NotNull
    @Valid
    public LocationCityTO location;

    @Override
    protected Location getLocationEntity() {
        return location.toEntity();
    }
}
