package net.readybid.app.core.action.traveldestination;

import net.readybid.app.core.entities.location.Location;
import net.readybid.app.core.entities.traveldestination.TravelDestinationType;
import net.readybid.user.BasicUserDetails;

public class CreateTravelDestinationRequest {

    public String rfpId;
    public TravelDestinationType type;
    public String name;
    public Integer estimatedRoomNights;
    public Long estimatedSpend;
    public Location location;
    public BasicUserDetails creator;
}
