package net.readybid.entities.consultancy.logic;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.entities.consultancy.core.Consultancy;
import net.readybid.entities.consultancy.core.ConsultancyImpl;
import net.readybid.location.LocationFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 1/7/2017.
 * 
 */
@Service
public class ConsultancyFactoryImpl implements ConsultancyFactory {

    final private LocationFactory locationFactory;

    @Autowired
    public ConsultancyFactoryImpl(LocationFactory locationFactory) {
        this.locationFactory = locationFactory;
    }

    @Override
    public Consultancy create(CreateEntityRequest request) {
        final ConsultancyImpl a = new ConsultancyImpl();
        a.setId(new ObjectId());
        a.setType(request.type);
        a.setName(request.name);
        a.setIndustry(request.industry);
        a.setWebsite(request.website);
        a.setLocation(locationFactory.createLocation(request.location));
        return a;
    }
}