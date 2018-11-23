package net.readybid.entities.company.logic;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.entities.company.core.Company;
import net.readybid.entities.company.core.CompanyImpl;
import net.readybid.location.LocationFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/5/2017.
 * 
 */
@Service
public class CompanyFactoryImpl implements CompanyFactory {

    final private LocationFactory locationFactory;

    @Autowired
    public CompanyFactoryImpl(LocationFactory locationFactory) {
        this.locationFactory = locationFactory;
    }

    @Override
    public Company create(CreateEntityRequest request) {
        final CompanyImpl c = new CompanyImpl();
        c.setId(new ObjectId());
        c.setType(request.type);
        c.setName(request.name);
        c.setIndustry(request.industry);
        c.setWebsite(request.website);
        c.setLocation(locationFactory.createLocation(request.location));
        return c;

    }
}