package net.readybid.entities.agency.logic;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.entities.agency.core.Agency;
import net.readybid.entities.agency.core.AgencyImpl;
import net.readybid.location.LocationFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/6/2017.
 * 
 */
@Service
public class AgencyFactoryImpl implements AgencyFactory {

    final private LocationFactory locationFactory;

    @Autowired
    public AgencyFactoryImpl(LocationFactory locationFactory) {
        this.locationFactory = locationFactory;
    }

    @Override
    public <S extends CreateEntityRequest> Agency create(S request) {
        final AgencyImpl a = new AgencyImpl();
        a.setId(new ObjectId());
        a.setType(request.type);
        a.setName(request.name);
        a.setIndustry(request.industry);
        a.setWebsite(request.website);
        a.setLocation(locationFactory.createLocation(request.location));
        return a;
    }
}