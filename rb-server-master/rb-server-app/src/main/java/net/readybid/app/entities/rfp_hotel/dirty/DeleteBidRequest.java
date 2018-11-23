package net.readybid.app.entities.rfp_hotel.dirty;

import net.readybid.user.BasicUserDetails;

public class DeleteBidRequest {

    public String destinationId;
    public BasicUserDetails currentUser;
    public String rfpId;

    public DeleteBidRequest(String rfpId, String destinationId, BasicUserDetails user) {
        this.rfpId = rfpId;
        this.destinationId = destinationId;
        this.currentUser = user;
    }
}
