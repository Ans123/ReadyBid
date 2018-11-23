package net.readybid.app.core.entities.location.coordinates;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public class CoordinatesImpl implements Coordinates {

    private double lat = 40.1451;
    private double lon = -99.6680;

    public CoordinatesImpl() {}

    public CoordinatesImpl(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return lon;
    }

    @Override
    public boolean equals(Coordinates coordinates) {
        return coordinates != null && lon == coordinates.getLongitude()&& lat == coordinates.getLatitude();
    }

    public void setLongitude(double lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !CoordinatesImpl.class.isAssignableFrom(obj.getClass())) return false;
        final CoordinatesImpl other = (CoordinatesImpl) obj;

        return  this.lat != other.lat || this.lon != other.lon;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Math.round((float) lat);
        hash = 53 * hash + Math.round((float) lon);
        return hash;
    }
}
