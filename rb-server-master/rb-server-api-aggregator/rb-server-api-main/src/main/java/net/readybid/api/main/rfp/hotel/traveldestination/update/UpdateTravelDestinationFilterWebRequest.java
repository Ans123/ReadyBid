package net.readybid.api.main.rfp.hotel.traveldestination.update;

import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationFilterRequest;
import net.readybid.location.MaxDistanceTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by DejanK on 1/23/2017.
 *
 */
public class UpdateTravelDestinationFilterWebRequest {

    @NotNull
    @Valid
    public MaxDistanceTO maxDistance;

    public List<String> amenities;

    public List<String> chains;

    public UpdateTravelDestinationFilterRequest getModel(String destinationId) {
        final UpdateTravelDestinationFilterRequest model = new UpdateTravelDestinationFilterRequest();
        model.destinationId = destinationId;
        model.maxDistance = maxDistance;
        model.amenities = amenities;
        model.chains = chains;
        return model;
    }
}
