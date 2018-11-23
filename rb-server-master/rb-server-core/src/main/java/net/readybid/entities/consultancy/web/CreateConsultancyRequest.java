package net.readybid.entities.consultancy.web;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.app.core.entities.entity.EntityType;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public class CreateConsultancyRequest extends CreateEntityRequest {
    public CreateConsultancyRequest(){
        type = EntityType.TRAVEL_CONSULTANCY;
    }
}
