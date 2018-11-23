package net.readybid.app.core.entities.location.distance;

import net.readybid.app.core.entities.location.coordinates.Coordinates;

/**
 * Created by DejanK on 1/12/2017.
 *
 */
public class DistanceImpl<T> implements Distance<T>{

    private double distance;
    private DistanceUnit distanceUnit;
    private T subject;

    public DistanceImpl() {}

    public DistanceImpl(Coordinates coordinates1, Coordinates coordinates2, DistanceUnit unit) {
        final double distanceInMeters = calculateDistance(
                coordinates1.getLatitude(),
                coordinates2.getLatitude(),
                coordinates1.getLongitude(),
                coordinates2.getLongitude()
        );
        distance = fromMeters(distanceInMeters, unit);
        this.distanceUnit = unit;
    }

    public DistanceImpl(double distance, DistanceUnit unit) {
        setDistance(distance, unit);
    }

    public void setDistance(double distance, DistanceUnit distanceUnit) {
        this.distance = distance;
        this.distanceUnit = distanceUnit;
    }

    public void setSubject(T subject) {
        this.subject = subject;
    }

    @Override
    public T getSubject() {
        return subject;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public DistanceUnit getDistanceUnit() {
        return distanceUnit;
    }

    @Override
    public double getDistance(DistanceUnit unit) {
        if(unit.equals(distanceUnit)){
            return distance;
        } else {
            return fromMeters(toMeters(), unit);
        }
    }

    /**
     * Calculate distance between two points in latitude and longitude. Uses Haversine method as its base.
     *
     * @return Distance in Meters
     */
    private double calculateDistance(double latitude1, double latitude2, double longitude1, double longitude2){
        final int R = 6371; // Radius of the earth

        Double latitudeDistance = Math.toRadians(latitude2 - latitude1);
        Double longitudeDistance = Math.toRadians(longitude2 - longitude1);
        Double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // convert to meters
    }

    private double fromMeters(double distanceInMeters, DistanceUnit unit) {
        switch (unit){
            case MI:
                return distanceInMeters / 1609.344;
            case KM:
                return distanceInMeters / 1000;
        }
        throw new RuntimeException("Unknown distance unit");
    }

    private double toMeters() {
        switch (distanceUnit){
            case MI:
                return distance * 1609.344;
            case KM:
                return distance * 1000;
        }
        throw new RuntimeException("Unknown distance unit");
    }

    public void setDistance(double value) {
        this.distance = value;
    }

    public void setDistanceUnit(DistanceUnit distanceUnit) {
        this.distanceUnit = distanceUnit;
    }
}
