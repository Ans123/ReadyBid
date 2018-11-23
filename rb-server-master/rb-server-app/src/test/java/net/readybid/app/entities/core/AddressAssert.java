package net.readybid.app.entities.core;

import net.readybid.app.core.entities.location.address.Address;
import net.readybid.app.core.entities.location.address.Country;
import net.readybid.test_utils.RbAbstractAssert;

public class AddressAssert extends RbAbstractAssert<AddressAssert, Address> {

    public static AddressAssert that(Address actual) {
        return new AddressAssert(actual);
    }

    private AddressAssert(Address actual) {
        super(actual, AddressAssert.class);
    }

    public AddressAssert hasAddress1(Object expected) {
        assertFieldEquals("address 1", actual.getAddress1(), expected);
        return this;
    }

    public AddressAssert hasAddress2(Object expected) {
        assertFieldEquals("address 2", actual.getAddress2(), expected);
        return this;
    }

    public AddressAssert hasCity(Object expected) {
        assertFieldEquals("city", actual.getCity(), expected);
        return this;
    }

    public AddressAssert hasCounty(Object expected) {
        assertFieldEquals("county", actual.getCounty(), expected);
        return this;
    }

    public AddressAssert hasState(Object expected) {
        assertFieldEquals("state", actual.getState(), expected);
        return this;
    }

    public AddressAssert hasRegion(Object expected) {
        assertFieldEquals("region", actual.getRegion(), expected);
        return this;
    }

    public AddressAssert hasZip(Object expected) {
        assertFieldEquals("zip", actual.getZip(), expected);
        return this;
    }

    public AddressAssert hasCountry(Object expected) {
        assertFieldEquals("country", actual.getCountry(), Country.valueOf(String.valueOf(expected)));
        return this;
    }
}
