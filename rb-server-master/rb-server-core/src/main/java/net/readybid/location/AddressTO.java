package net.readybid.location;

import net.readybid.app.core.entities.location.address.AddressImpl;
import net.readybid.app.core.entities.location.address.Country;
import net.readybid.utils.RbStringUtils;
import net.readybid.validators.Text100;
import net.readybid.validators.Text20;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by DejanK on 11/11/2016.
 *
 */

public class AddressTO {

    @NotBlank
    @Text100
    public String address1;

    @Text100
    public String address2;

    @NotBlank
    @Text100
    public String city;

    @Text100
    public String county;

    @Size(max = 2)
    public String state;

    @Text100
    public String region;

    @NotNull
    public Country country;

    @Text20
    public String zip;

    AddressImpl load() {
        final AddressImpl address = new AddressImpl();

        address.setAddress1(RbStringUtils.safeTrim(address1));
        address.setAddress2(RbStringUtils.safeTrim(address2));
        address.setCity(RbStringUtils.safeTrim(city));
        address.setCounty(RbStringUtils.safeTrim(county));
        address.setState(state);
        address.setRegion(RbStringUtils.safeTrim(region));
        address.setZip(RbStringUtils.safeTrim(zip));
        address.setCountry(country);

        return address;
    }
}
