package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;

public class AccountCollection implements IdField, StatusFields, CreatedFields {

    public static final String COLLECTION_NAME = "Account";

    public static final String ENTITY_ID = "entityId";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String INDUSTRY = "industry";
    public static final String WEBSITE = "website";
    public static final String LOCATION = "location";
    public static final String LOGO = "logo";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String PHONE = "phone";
    public static final String CREATED = "created";
    public static final String STATUS = "status";
    public static final String CHANGED = "changed";

}
