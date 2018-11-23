package net.readybid.app.core.service.traveldestination;

import net.readybid.app.core.entities.traveldestination.TravelDestination;

import java.util.List;
import java.util.Map;

public class ListTravelDestinationsResult {

    public String rfpId;
    public String rfpName;
    public final List<? extends TravelDestination> destinations;
    public final Map<String, Long> bidsPerTravelDestination;

    public ListTravelDestinationsResult(List<? extends TravelDestination> destinations, Map<String, Long> bidsPerTravelDestination) {
        this.destinations = destinations;
        this.bidsPerTravelDestination = bidsPerTravelDestination;
    }
}
