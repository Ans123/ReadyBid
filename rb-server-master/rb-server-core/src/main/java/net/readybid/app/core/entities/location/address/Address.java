package net.readybid.app.core.entities.location.address;

/**
 * Created by DejanK on 11/10/2016.
 *
 */
public interface Address {
    String getFullAddress();

    String getAddress1();

    String getAddress2();

    String getCity();

    String getStateOrRegion();

    String getZip();

    Country getCountry();

    String getCounty();

    String getShortAddress();

    String getState();

    String getRegion();

    String printCountry();

    String printStateOrRegion();

    String printCity();

    String getCountryName();
}
