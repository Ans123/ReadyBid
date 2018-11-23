package net.readybid.entities.hmc;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.location.LocationFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
@Service
public class HotelManagementCompanyFactoryImpl implements HotelManagementCompanyFactory {

    final private LocationFactory locationFactory;

    @Autowired
    public HotelManagementCompanyFactoryImpl(LocationFactory locationFactory) {
        this.locationFactory = locationFactory;
    }

    @Override
    public HotelManagementCompany create(CreateEntityRequest request) {
        final HotelManagementCompanyImpl c = new HotelManagementCompanyImpl();
        c.setId(new ObjectId());
        c.setType(request.type);
        c.setName(request.name);
        c.setIndustry(request.industry);
        c.setWebsite(request.website);
        c.setLocation(locationFactory.createLocation(request.location));
        return c;
    }
}
