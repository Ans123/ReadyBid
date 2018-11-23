package net.readybid.entities.agency.web;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.app.core.entities.entity.EntityType;

/**
 * Created by DejanK on 1/6/2017.
 *
 */
public class CreateAgencyRequest extends CreateEntityRequest {
    public CreateAgencyRequest(){
        type = EntityType.TRAVEL_AGENCY;
    }
}
