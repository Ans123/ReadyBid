package net.readybid.entities.consultancy.logic;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.entities.consultancy.core.Consultancy;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public interface ConsultancyRepository {

    Consultancy findById(String id);

    void saveForValidation(Consultancy consultancy);

    Consultancy findByIdIncludingUnverified(ObjectId id, String... fields);

    Entity findByIdIncludingUnverified(String id, String... fields);
}
