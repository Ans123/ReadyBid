package net.readybid.entities.core;

import net.readybid.utils.CreationDetails;
import net.readybid.utils.StatusDetails;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class EntityStatusDetails extends StatusDetails<EntityStatus> {

    public EntityStatusDetails() {}

    public EntityStatusDetails(CreationDetails created, EntityStatus status) {
        super(created, status);
    }
}
