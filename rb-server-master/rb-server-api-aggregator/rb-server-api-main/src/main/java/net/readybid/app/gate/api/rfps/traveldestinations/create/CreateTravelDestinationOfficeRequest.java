package net.readybid.app.gate.api.rfps.traveldestinations.create;

import net.readybid.app.core.entities.location.Location;
import net.readybid.location.LocationTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public class CreateTravelDestinationOfficeRequest extends CreateTravelDestinationWebRequest {

    @NotNull
    @Valid
    public LocationTO location;

    @Override
    protected Location getLocationEntity() {
        return location.load();
    }
}