package net.readybid.app.gate.api.rfps.traveldestinations.create;

import net.readybid.app.core.entities.location.Location;
import net.readybid.location.LocationCityTO;

import javax.validation.constraints.NotNull;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public class CreateTravelDestinationBulkItemRequest extends CreateTravelDestinationWebRequest {

    @NotNull
    public LocationCityTO location;

    @Override
    protected Location getLocationEntity() {
        return location.toEntity();
    }
}