package net.readybid.app.core.entities.traveldestination;

import net.readybid.app.core.entities.location.distance.Distance;
import net.readybid.app.core.entities.location.distance.DistanceImpl;
import net.readybid.app.core.entities.location.distance.DistanceUnit;

import java.util.Arrays;
import java.util.List;

/**
 * Created by DejanK on 1/23/2017.
 *
 */
public class TravelDestinationHotelFilter {

    public Distance maxDistance;
    public List<String> amenities;
    public List<String> chains;

    public TravelDestinationHotelFilter(){
        maxDistance = new DistanceImpl<>(5, DistanceUnit.MI);
    }
}