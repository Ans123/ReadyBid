package net.readybid.app.persistence.mongodb.repository.mapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class EntityCollection {

    public static final boolean INCLUDE_UNVALIDATED = true;

    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String INDUSTRY = "industry";
    public static final String WEBSITE = "website";
    public static final String LOCATION = "location";
    public static final String IMAGE = "image";
    public static final String IMAGE_URL = "image.url";
    public static final String LOGO = "logo";
    public static final String CREATED = "created";
    public static final String STATUS = "status";

    public static final List<String> ALL_FIELDS = Collections.unmodifiableList(Arrays.asList(
            TYPE, NAME, INDUSTRY, WEBSITE, LOCATION, IMAGE, LOGO, CREATED, STATUS
    ));

    protected EntityCollection(){}

}
