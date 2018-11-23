package net.readybid.entities.chain;

/**
 * Created by DejanK on 2/15/2017.
 *
 */

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.location.LocationFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelChainFactoryImpl implements HotelChainFactory {

    final private LocationFactory locationFactory;

    @Autowired
    public HotelChainFactoryImpl(LocationFactory locationFactory) {
        this.locationFactory = locationFactory;
    }

    @Override
    public HotelChain create(CreateEntityRequest request) {
        final HotelChainImpl c = new HotelChainImpl();
        c.setId(new ObjectId());
        c.setType(request.type);
        c.setName(request.name);
        c.setIndustry(request.industry);
        c.setWebsite(request.website);
        c.setLocation(locationFactory.createLocation(request.location));
        return c;
    }
}
