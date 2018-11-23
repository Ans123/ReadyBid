package net.readybid.entities.hotel.web;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.app.core.entities.entity.EntityType;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
public class CreateHotelRequest extends CreateEntityRequest {
    public CreateHotelRequest(){
        type = EntityType.HOTEL;
    }
}
