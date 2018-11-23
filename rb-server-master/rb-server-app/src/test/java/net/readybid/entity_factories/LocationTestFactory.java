package net.readybid.entity_factories;

import net.readybid.app.core.entities.location.LocationImpl;
import net.readybid.app.core.entities.location.address.Address;

public class LocationTestFactory {

    private LocationTestFactory(){}

    public static LocationImpl random() {
        return random(AddressTestFactory.random());
    }

    public static LocationImpl randomAll() {
        return random(AddressTestFactory.randomAll());
    }

    private static LocationImpl random(Address address) {
        final LocationImpl location = new LocationImpl();

        location.setAddress(address);
        location.setCoordinates(CoordinatesTestFactory.random());
        location.setFullAddress(address.getFullAddress());

        return location;

    }
}
