package net.readybid.entities.hmc;

import net.readybid.entities.core.EntityImpl;
import net.readybid.app.core.entities.entity.EntityType;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
public class HotelManagementCompanyImpl extends EntityImpl implements HotelManagementCompany {

    public HotelManagementCompanyImpl(){
        setType(EntityType.HMC);
    }

}
