package net.readybid.core.rfp.hotel.bid;

import net.readybid.app.core.entities.traveldestination.TravelDestination;

public class UpdateBidTravelDestinationRequest {

    final public String destinationId;
    final public TravelDestination destination;
    final public boolean hasDestinationCoordinatesChanged;

    public UpdateBidTravelDestinationRequest(TravelDestination destination, boolean hasDestinationCoordinatesChanged) {
        this.destination = destination;
        this.destinationId = destination.getId();
        this.hasDestinationCoordinatesChanged = hasDestinationCoordinatesChanged;
    }
}
