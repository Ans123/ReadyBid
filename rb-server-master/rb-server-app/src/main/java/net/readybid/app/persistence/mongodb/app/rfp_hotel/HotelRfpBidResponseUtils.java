package net.readybid.app.persistence.mongodb.app.rfp_hotel;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.location.address.Address;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.set;

class HotelRfpBidResponseUtils {

    private HotelRfpBidResponseUtils(){}

    static List<Bson> createEntityUpdates(Entity entity, String fieldPrefix){
        if(entity == null) return null;
        final Address address = entity.getAddress();
        final List<Bson> updateList = new ArrayList<>();
        final String keyTemplate = fieldPrefix == null || fieldPrefix.isEmpty() ? "%s" : fieldPrefix + ".%s";

        addIfNotNull(entity.getName(), String.format(keyTemplate, "PROPNAME"), updateList);
        addIfNotNull(entity.getWebsite(), String.format(keyTemplate, "PROPURL"), updateList);

        addIfNotNull(address.getAddress1(), String.format(keyTemplate, "PROPADD1"), updateList);
        addIfNotNull(address.getAddress2(), String.format(keyTemplate, "PROPADD2"), updateList);
        addIfNotNull(address.getCity(), String.format(keyTemplate, "PROPCITY"), updateList);
        addIfNotNull(address.getState(), String.format(keyTemplate, "PROPSTATEPROV"), updateList);
        addIfNotNull(address.getCounty(), String.format(keyTemplate, "PROPCOUNTY"), updateList);
        addIfNotNull(address.getRegion(), String.format(keyTemplate, "PROPREGION"), updateList);
        addIfNotNull(address.getCountryName(), String.format(keyTemplate, "PROPCOUNTRY"), updateList);
        addIfNotNull(address.getZip(), String.format(keyTemplate, "PROPPOSTCODE"), updateList);

        return updateList;
    }

    static List<Bson> createAddressUpdates(Address address, String fieldPrefix){
        if(address == null) return null;
        final List<Bson> updateList = new ArrayList<>();
        final String keyTemplate = fieldPrefix == null || fieldPrefix.isEmpty() ? "%s" : fieldPrefix + ".%s";

        addIfNotNull(address.getAddress1(), String.format(keyTemplate, "PROPADD1"), updateList);
        addIfNotNull(address.getAddress2(), String.format(keyTemplate, "PROPADD2"), updateList);
        addIfNotNull(address.getCity(), String.format(keyTemplate, "PROPCITY"), updateList);
        addIfNotNull(address.getState(), String.format(keyTemplate, "PROPSTATEPROV"), updateList);
        addIfNotNull(address.getCounty(), String.format(keyTemplate, "PROPCOUNTY"), updateList);
        addIfNotNull(address.getRegion(), String.format(keyTemplate, "PROPREGION"), updateList);
        addIfNotNull(address.getCountryName(), String.format(keyTemplate, "PROPCOUNTRY"), updateList);
        addIfNotNull(address.getZip(), String.format(keyTemplate, "PROPPOSTCODE"), updateList);

        return updateList;
    }

    static void addIfNotNull(Object o, String field, List<Bson> updates){
        if(o != null) updates.add(set(field, o));
    }
}
