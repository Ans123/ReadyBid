package net.readybid.entities.chain;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.app.core.entities.entity.EntityType;

/**
 * Created by DejanK on 2/15/2017.
 */
public class CreateChainRequest extends CreateEntityRequest {
    public CreateChainRequest(){
        type = EntityType.CHAIN;
    }

}
