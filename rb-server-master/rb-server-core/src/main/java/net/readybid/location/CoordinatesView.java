package net.readybid.location;


import net.readybid.app.core.entities.location.coordinates.Coordinates;
import net.readybid.app.core.entities.location.coordinates.CoordinatesImpl;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 12/26/2016.
 *
 */
public class CoordinatesView implements ViewModel<Coordinates> {

    public static final ViewModelFactory<Coordinates, CoordinatesView> FACTORY = CoordinatesView::new;

    public double lat;
    public double lng;

    public CoordinatesView() {}

    public CoordinatesView(Coordinates coordinates) {
        final CoordinatesImpl c = (CoordinatesImpl) coordinates;
        lat = c.getLatitude();
        lng = c.getLongitude();
    }
}
