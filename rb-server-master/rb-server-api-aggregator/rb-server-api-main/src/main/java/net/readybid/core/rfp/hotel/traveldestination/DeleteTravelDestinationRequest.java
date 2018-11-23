package net.readybid.core.rfp.hotel.traveldestination;

import net.readybid.user.BasicUserDetails;

public class DeleteTravelDestinationRequest {

    public String destinationId;
    public BasicUserDetails currentUser;

    public DeleteTravelDestinationRequest(String destinationId, BasicUserDetails user) {
        this.destinationId = destinationId;
        this.currentUser = user;
    }
}
