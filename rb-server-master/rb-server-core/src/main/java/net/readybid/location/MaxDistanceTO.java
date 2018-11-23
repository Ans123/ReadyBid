package net.readybid.location;

import net.readybid.app.core.entities.location.distance.DistanceUnit;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Created by DejanK on 1/12/2017.
 *
 */
public class MaxDistanceTO {

    @DecimalMin(value = "0.1")
    @DecimalMax(value = "50")
    public double value;

    @NotNull
    public DistanceUnit unit;

    public double inMeters() { return (unit == DistanceUnit.KM ? value : value * 1.609344D) * 1000; }
}
