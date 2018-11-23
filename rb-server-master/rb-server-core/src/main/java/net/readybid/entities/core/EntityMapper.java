package net.readybid.entities.core;

import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.location.Location;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.AbstractMap;
import java.util.Map;

import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.unset;

public class EntityMapper {

    public static final String NAME = "name";
    public static final String LOCATION = "location";
    public static final String INDUSTRY = "industry";
    public static final String WEBSITE = "website";
    public static final String LOGO = "logo";
    public static final String IMAGE = "image";

    private static EntityMapper instance;

    public static EntityMapper getInstance() {
        if(instance == null){
            instance = new EntityMapper();
        }
        return instance;
    }

    private LocationMapper locationMapper = LocationMapper.getInstance();
    private EntityImageMapper entityImageMapper = EntityImageMapper.getInstance();

    private EntityMapper(){}

    public Bson setName(String name){
        return name == null ? unset(NAME) : set(NAME, name);
    }

    public Map.Entry<String, Object> updateName(String name) {
        return name == null ? null : new AbstractMap.SimpleImmutableEntry<>(NAME, name);
    }

    public Map.Entry<String, Object> updateLocation(Location location){
        final Document d = locationMapper.toDocumentForElasticSearch(location);
        return d == null ? null : new AbstractMap.SimpleImmutableEntry<>(LOCATION, d);
    }

    public Bson setIndustry(EntityIndustry industry) {
        return industry == null ? unset(INDUSTRY) : set(INDUSTRY, String.valueOf(industry));
    }

    public Bson setWebsite(String website) {
        return website == null ? unset(WEBSITE) : set(WEBSITE, website);
    }

    public Bson setLogo(String logo) {
        return logo == null || logo.isEmpty()? unset(LOGO) : set(LOGO, logo);
    }

    public Bson setImage(String image) {
        return image == null || image.isEmpty()
                ? unset(IMAGE)
                : set(IMAGE, entityImageMapper.toDocument(() -> image));
    }
}
