package net.readybid.app.entities.core;

import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.test_utils.RbAbstractAssert;

public class CoordinatesAssert extends RbAbstractAssert<CoordinatesAssert, Coordinates> {

    public static CoordinatesAssert that(Coordinates actual) {
        return new CoordinatesAssert(actual);
    }

    private CoordinatesAssert(Coordinates actual) {
        super(actual, CoordinatesAssert.class);
    }

    public CoordinatesAssert hasLongitude(Object expected) {
        assertFieldEquals("longitude", actual.getLongitude(), expected);
        return this;
    }

    public CoordinatesAssert hasLatitude(Object expected) {
        assertFieldEquals("latitude", actual.getLatitude(), expected);
        return this;
    }
}
