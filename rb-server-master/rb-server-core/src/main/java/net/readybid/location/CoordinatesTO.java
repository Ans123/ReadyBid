package net.readybid.location;

import net.readybid.app.core.entities.location.coordinates.CoordinatesImpl;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DejanK on 11/11/2016.
 *
 */
public class CoordinatesTO {

    @NotNull
    public Float lng;

    @NotNull
    public Float lat;

    CoordinatesImpl load() {
        return new CoordinatesImpl(lat, lng);
    }

    public List toList() { return Arrays.asList(lng, lat); }
}
