package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;

public class UserCollection implements IdField, StatusFields, CreatedFields {

    public static final String COLLECTION_NAME = "User";

    public static final String CHANGED = "changed";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String FIRST_NAME = "firstName";
    public static final String FULL_NAME = "fullName";
    public static final String LAST_NAME = "lastName";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";
    public static final String PROFILE_PICTURE = "profilePicture";
}
