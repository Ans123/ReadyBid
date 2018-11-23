package net.readybid.location;


import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.address.AddressImpl;
import net.readybid.app.core.entities.location.address.Country;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 12/26/2016.
 *
 */
public class AddressView implements ViewModel<Address> {

    public static final ViewModelFactory<Address, AddressView> FACTORY = AddressView::new;
    public String address1;
    public String address2;
    public String city;
    public String county;
    public String state;
    public String region;
    public String zip;
    public Country country;
    public String countryFullName;

    public AddressView() {}

    public AddressView(Address address) {
        AddressImpl a = (AddressImpl) address;
        address1 = a.getAddress1();
        address2 = a.getAddress2();
        city = a.getCity();
        county = a.getCounty();
        state = a.getState();
        region = a.getRegion();
        zip = a.getZip();
        country = a.getCountry();
        countryFullName = country == null ? null : country.getFullName();
    }
}
