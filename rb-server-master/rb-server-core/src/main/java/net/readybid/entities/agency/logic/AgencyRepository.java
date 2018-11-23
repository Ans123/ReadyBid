package net.readybid.entities.agency.logic;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.entities.agency.core.Agency;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/6/2017.
 *
 */
public interface AgencyRepository {

    Agency findById(String id);

    void saveForValidation(Agency agency);

    Agency findByIdIncludingUnverified(ObjectId id, String... fields);

    Entity findByIdIncludingUnverified(String id, String... fields);
}
