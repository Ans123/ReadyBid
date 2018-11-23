package net.readybid.location;


import net.readybid.app.core.entities.location.Location;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 12/26/2016.
 *
 */
public class LocationViewModel implements ViewModel<Location> {

    public static final ViewModelFactory<Location, LocationViewModel> FACTORY = LocationViewModel::new;

    public AddressView address;
    public String fullAddress;
    public CoordinatesView coordinates;

    public LocationViewModel() {}

    public LocationViewModel(Location location) {
        address = AddressView.FACTORY.createAsPartial(location.getAddress());
        fullAddress = location.getFullAddress();
        coordinates = CoordinatesView.FACTORY.createAsPartial(location.getCoordinates());
    }
}
