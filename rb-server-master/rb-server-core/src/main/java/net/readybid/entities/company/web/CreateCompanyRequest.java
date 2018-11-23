package net.readybid.entities.company.web;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.app.core.entities.entity.EntityType;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class CreateCompanyRequest extends CreateEntityRequest {
    public CreateCompanyRequest(){
        type = EntityType.COMPANY;
    }
}
