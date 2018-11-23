package net.readybid.entities.hmc;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.app.core.entities.entity.EntityType;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
public class CreateHotelManagementCompanyRequest extends CreateEntityRequest {
    public CreateHotelManagementCompanyRequest(){
        type = EntityType.HMC;
    }
}
