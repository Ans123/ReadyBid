package net.readybid.entity_factories;

import net.readybid.app.core.entities.location.coordinates.CoordinatesImpl;
import net.readybid.test_utils.RbRandom;

public class CoordinatesTestFactory {

    private CoordinatesTestFactory(){}

    public static CoordinatesImpl random() {

        final CoordinatesImpl coordinates = new CoordinatesImpl();

        coordinates.setLatitude(RbRandom.randomDouble());
        coordinates.setLongitude(RbRandom.randomDouble());

        return coordinates;
    }
}
