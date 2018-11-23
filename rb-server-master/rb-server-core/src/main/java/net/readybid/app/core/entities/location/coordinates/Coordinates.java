package net.readybid.app.core.entities.location.coordinates;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public interface Coordinates {
    double getLatitude();

    double getLongitude();

    boolean equals(Coordinates coordinates);
}
