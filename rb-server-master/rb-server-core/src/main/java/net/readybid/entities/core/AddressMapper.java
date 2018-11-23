package net.readybid.entities.core;

import net.readybid.app.core.entities.location.address.Address;
import net.readybid.mongodb.RbDocument;
import org.bson.Document;


public class AddressMapper {

    public static final String ADDRESS_1 = "address1";
    public static final String ADDRESS_2 = "address2";
    public static final String CITY = "city";
    public static final String REGION = "region";
    public static final String STATE = "state";
    public static final String ZIP = "zip";
    public static final String COUNTRY = "country";
    public static final String COUNTRY_NAME = "countryName";

    private static AddressMapper instance;

    public static AddressMapper getInstance() {
        if(instance == null){
            instance = new AddressMapper();
        }
        return instance;
    }

    public Document toDocument(Address address) {
        if(address == null) return null;
        return toBasicDocument(address);
    }

    public Document toDocumentForElasticSearch(Address address) {
        if(address == null) return null;
        final RbDocument d = toBasicDocument(address);
        d.putIfNotNull(COUNTRY_NAME, address.getCountryName());
        return d;
    }

    private RbDocument toBasicDocument(Address address) {
        final RbDocument d = new RbDocument();

        d.putIfNotNull(ADDRESS_1, address.getAddress1());
        d.putIfNotNull(ADDRESS_2, address.getAddress2());
        d.putIfNotNull(CITY, address.getCity());
        d.putIfNotNull(REGION, address.getRegion());
        d.putIfNotNull(STATE, address.getState());
        d.putIfNotNull(ZIP, address.getZip());
        d.putIfNotNull(COUNTRY, String.valueOf(address.getCountry()));

        return d;
    }
}
