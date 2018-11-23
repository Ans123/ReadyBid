package net.readybid.location;

import net.readybid.app.core.entities.location.*;
import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.address.AddressImpl;
import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.app.core.entities.location.coordinates.CoordinatesImpl;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
@Service
public class LocationFactoryImpl implements LocationFactory {

    @Override
    public Address createAddress(AddressTO to) {
        final AddressImpl address = new AddressImpl();
        address.setAddress1(to.address1);
        address.setAddress2(to.address2);
        address.setCity(to.city);
        address.setCounty(to.county);
        address.setState(to.state);
        address.setRegion(to.region);
        address.setZip(to.zip);
        address.setCountry(to.country);

        return address;
    }

    @Override
    public Coordinates createCoordinates(CoordinatesTO coordinates) {
        return new CoordinatesImpl(coordinates.lat, coordinates.lng);
    }

    @Override
    public Location createLocation(LocationTO to) {
        return createLocation(createAddress(to.address), to.coordinates);
    }

    private Location createLocation(Address address, CoordinatesTO coordinates) {
        final LocationImpl location = new LocationImpl();
        location.setAddress(address);
        location.updateFullAddress();
        location.setCoordinates(createCoordinates(coordinates));

        return location;
    }
}
