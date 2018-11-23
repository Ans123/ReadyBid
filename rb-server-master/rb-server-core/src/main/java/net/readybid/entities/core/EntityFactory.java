package net.readybid.entities.core;

import net.readybid.app.core.entities.entity.Entity;

/**
 * Created by DejanK on 3/24/2017.
 *
 */
public interface EntityFactory<T extends Entity> {

    <S extends CreateEntityRequest> T create(S request);
}
