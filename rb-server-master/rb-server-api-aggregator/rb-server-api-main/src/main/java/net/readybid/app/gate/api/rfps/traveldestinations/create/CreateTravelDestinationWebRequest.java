package net.readybid.app.gate.api.rfps.traveldestinations.create;

import net.readybid.app.core.action.traveldestination.CreateTravelDestinationRequest;
import net.readybid.app.core.entities.location.Location;
import net.readybid.app.core.entities.traveldestination.TravelDestinationType;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.core.rfp.hotel.traveldestination.UpdateTravelDestinationRequest;
import net.readybid.user.BasicUserDetails;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by DejanK on 1/11/2017.
 *
 */
public abstract class CreateTravelDestinationWebRequest {

    @NotNull
    public TravelDestinationType type;

    @NotBlank
    @Size(max = 100)
    public String name;

    @Max(1000000)
    public Integer estimatedRoomNights;

    @Max(100000000)
    public Long estimatedSpend;

    public CreateTravelDestinationRequest getCreateModel(String rfpId, BasicUserDetails currentUser) {
        final CreateTravelDestinationRequest model = new CreateTravelDestinationRequest();
        fillModel(rfpId, currentUser, model);
        return model;
    }

    public UpdateTravelDestinationRequest getUpdateModel(String rfpId, String destinationId, AuthenticatedUser currentUser) {
        final UpdateTravelDestinationRequest model = new UpdateTravelDestinationRequest();
        fillModel(rfpId, currentUser, model);
        model.destinationId = destinationId;
        return model;
    }

    private void fillModel(String rfpId, BasicUserDetails currentUser, CreateTravelDestinationRequest model) {
        model.rfpId = rfpId;
        model.type = type;
        model.name = name;
        model.estimatedRoomNights = estimatedRoomNights;
        model.estimatedSpend = estimatedSpend;
        model.location = getLocationEntity();
        model.creator = currentUser;
    }

    protected abstract Location getLocationEntity();
}