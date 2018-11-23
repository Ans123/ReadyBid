package net.readybid.mongodb;

import java.util.LinkedList;

/**
 * Created by DejanK on 1/5/2017.
 *
 */
public class GeoJsonPoint {

    public final String type = "Point";
    public final LinkedList<Double> coordinates;

    public GeoJsonPoint(double longitude, double latitude){
        coordinates = new LinkedList<>();
        coordinates.add(longitude);
        coordinates.add(latitude);
    }
}