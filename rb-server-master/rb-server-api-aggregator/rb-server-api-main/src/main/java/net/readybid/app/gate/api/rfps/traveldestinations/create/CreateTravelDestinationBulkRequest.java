package net.readybid.app.gate.api.rfps.traveldestinations.create;

import net.readybid.app.core.action.traveldestination.CreateTravelDestinationRequest;
import net.readybid.user.BasicUserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class CreateTravelDestinationBulkRequest {

    @NotNull
    @Size(min=1, max=100)
    public List<CreateTravelDestinationBulkItemRequest> travelDestinations;

    public List<CreateTravelDestinationRequest> getModels(String rfpId, BasicUserDetails user) {
        final List<CreateTravelDestinationRequest> models = new ArrayList<>();
        for(CreateTravelDestinationBulkItemRequest r : travelDestinations){
            models.add(r.getCreateModel(rfpId, user));
        }
        return models;
    }
}
