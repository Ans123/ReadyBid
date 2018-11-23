package net.readybid.app.entities.core;

import net.readybid.app.core.entities.location.Location;
import net.readybid.test_utils.RbAbstractAssert;

import java.util.function.Consumer;

public class LocationAssert extends RbAbstractAssert<LocationAssert, Location> {

    public static LocationAssert that(Location actual) {
        return new LocationAssert(actual);
    }

    private LocationAssert(Location actual) {
        super(actual, LocationAssert.class);
    }

    public LocationAssert hasFullAddress(Object expected) {
        assertFieldEquals("full address", actual.getFullAddress(), expected);
        return this;
    }

    public LocationAssert hasAddress(Consumer<AddressAssert> consumer) {
        consumer.accept(AddressAssert.that(actual.getAddress()));
        return this;
    }


    public LocationAssert hasCoordinates(Consumer<CoordinatesAssert> consumer) {
        consumer.accept(CoordinatesAssert.that(actual.getCoordinates()));
        return this;
    }
}
