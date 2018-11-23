package net.readybid.entities.company.logic;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.entities.company.core.Company;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public interface CompanyRepository {

    Company findById(String id);

    void saveForValidation(Company company);

    Company findByIdIncludingUnverified(ObjectId id, String... fields);

    Entity findByIdIncludingUnverified(String id, String... fields);
}
