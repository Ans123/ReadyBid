package net.readybid.entities.hmc;

import net.readybid.app.core.entities.entity.Entity;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
public interface HotelManagementCompanyRepository {
    HotelManagementCompany findById(String id);

    void saveForValidation(HotelManagementCompany entity);

    HotelManagementCompany findByIdIncludingUnverified(String id);

    Entity findByIdIncludingUnverified(String id, String... fields);
}
