package net.readybid.entity_factories;

import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.address.AddressImpl;
import net.readybid.app.core.entities.location.address.Country;
import net.readybid.test_utils.RbRandom;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddressTestFactory {

    private final static List<Country> COUNTRIES = Collections.unmodifiableList(Arrays.asList(Country.US, Country.CA, Country.RS));

    private AddressTestFactory(){}

    public static AddressImpl random() {
        final AddressImpl address = new AddressImpl();

        address.setAddress1(RbRandom.name());
        address.setAddress2(RbRandom.name());
        address.setCity(RbRandom.name());
        address.setZip(RbRandom.alphanumeric(10, false));

        final Country country = COUNTRIES.get(RbRandom.randomInt(COUNTRIES.size()));
        address.setCountry(country);
        if(country.equals(Country.US)){
            address.setState(RbRandom.name());
            address.setCounty(RbRandom.name());
        } else if (country.equals(Country.CA)){
            address.setState(RbRandom.name());
        } else {
            address.setRegion(RbRandom.name());
        }

        return address;
    }

    public static Address randomAll() {
        final AddressImpl address = new AddressImpl();

        address.setAddress1(RbRandom.name());
        address.setAddress2(RbRandom.name());
        address.setCity(RbRandom.name());
        address.setZip(RbRandom.alphanumeric(10, false));
        address.setCounty(RbRandom.name());
        address.setState(RbRandom.name());
        address.setRegion(RbRandom.name());
        address.setCountry(RbRandom.randomEnum(Country.class));

        return address;
    }
}
