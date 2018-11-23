package net.readybid.entities.hotel.logic;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.entities.hotel.core.HotelImpl;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.location.LocationFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
@Service
public class HotelFactoryImpl implements HotelFactory {

    private final LocationFactory locationFactory;

    @Autowired
    public HotelFactoryImpl(LocationFactory locationFactory) {
        this.locationFactory = locationFactory;
    }

    @Override
    public Hotel create(CreateEntityRequest createHotelRequest) {
        final HotelImpl hotel = new HotelImpl();
        hotel.setId(new ObjectId());
        hotel.setType(createHotelRequest.type);
        hotel.setName(createHotelRequest.name);
        hotel.setIndustry(createHotelRequest.industry);
        hotel.setWebsite(createHotelRequest.website);
        hotel.setLocation(locationFactory.createLocation(createHotelRequest.location));
        hotel.setAnswers(createDefaultAnswers(hotel));
        return hotel;
    }

    private Map<String, String> createDefaultAnswers(HotelImpl hotel) {
        final Map<String, String> answers = new HashMap<>();
        final Address address = hotel.getAddress();
        answers.put("PROPNAME", hotel.getName());

        addIfExists(answers, "PROPADD1", address.getAddress1());
        addIfExists(answers, "PROPADD2", address.getAddress2());
        addIfExists(answers, "PROPCITY", address.getCity());
        addIfExists(answers, "PROPSTATEPROV", address.getState());
        addIfExists(answers, "PROPCOUNTY", address.getCounty());
        addIfExists(answers, "PROPREGION", address.getRegion());
        addIfExists(answers, "PROPCOUNTRY", address.getCountry().getFullName());
        addIfExists(answers, "PROPPOSTCODE", address.getZip());
        addIfExists(answers, "PROPURL", hotel.getWebsite());

        return answers;
    }

    private void addIfExists(Map<String, String> answers, String key, String value) {
        if( !(null == value || value.isEmpty()) ){
            answers.put(key, value);
        }
    }
}
