package net.readybid.entities.core;

import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.mongodb.GeoJsonPoint;
import net.readybid.mongodb.RbDocument;
import org.bson.Document;


public class CoordinatesMapper {

    private static CoordinatesMapper instance;

    public static CoordinatesMapper getInstance() {
        if(instance == null) instance = new CoordinatesMapper();
        return instance;
    }

    public Document toDocument(Coordinates coordinates) {
        final RbDocument d = new RbDocument();

        d.put("lat", coordinates.getLatitude());
        d.put("lng", coordinates.getLongitude());
        d.put("point", new GeoJsonPoint(coordinates.getLongitude(), coordinates.getLatitude()));

        return d;
    }
}
