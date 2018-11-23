package net.readybid.api.hotelrfp.traveldestination;

import net.readybid.location.CoordinatesTO;
import net.readybid.location.MaxDistanceTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by DejanK on 1/12/2017.
 *
 */
public class SearchForHotelsRequest {

    @NotNull
    @Valid
    public CoordinatesTO coordinates;

    @NotNull
    @Valid
    public MaxDistanceTO maxDistance;

    public List<String> chains;

    public List getCoordinatesAsList() {
        return coordinates.toList();
    }

    public double getMaxDistanceInMeters() {
        return maxDistance.inMeters();
    }

    public List<String> getChains() {
        return chains;
    }
}
