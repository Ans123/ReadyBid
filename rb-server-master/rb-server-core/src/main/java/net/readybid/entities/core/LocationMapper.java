package net.readybid.entities.core;

import net.readybid.app.core.entities.location.Location;
import net.readybid.mongodb.RbDocument;
import org.bson.Document;

public class LocationMapper {

    public static final String ADDRESS = "address";
    public static final String COORDINATES = "coordinates";
    public static final String FULL_ADDRESS = "fullAddress";

    private static LocationMapper instance;

    private static AddressMapper addressMapper = AddressMapper.getInstance();
    private static CoordinatesMapper coordinatesMapper = CoordinatesMapper.getInstance();

    public static LocationMapper getInstance() {
        if(instance == null){
            instance = new LocationMapper();
        }
        return instance;
    }

    public Document toDocumentForElasticSearch(Location l) {
        if(l == null) return null;
        final RbDocument d = new RbDocument();

        d.putIfNotNull(ADDRESS, addressMapper.toDocumentForElasticSearch(l.getAddress()));
        d.putIfNotNull(FULL_ADDRESS, l.getFullAddress());

        return d;
    }
}
