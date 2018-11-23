package net.readybid.core.rfp.hotel.traveldestination;

import net.readybid.location.MaxDistanceTO;

import java.util.List;

/**
 * Created by DejanK on 1/23/2017.
 *
 */
public class UpdateTravelDestinationFilterRequest {

    public String destinationId;

    public MaxDistanceTO maxDistance;
    public List<String> amenities;
    public List<String> chains;
}
