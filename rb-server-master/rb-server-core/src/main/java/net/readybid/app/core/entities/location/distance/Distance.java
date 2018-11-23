package net.readybid.app.core.entities.location.distance;

/**
 * Created by DejanK on 1/12/2017.
 *
 */
public interface Distance<T> {

    T getSubject();

    double getDistance();

    DistanceUnit getDistanceUnit();

    double getDistance(DistanceUnit unit);
}
