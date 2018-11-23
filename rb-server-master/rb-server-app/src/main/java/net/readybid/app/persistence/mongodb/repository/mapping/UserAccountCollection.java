package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;

public class UserAccountCollection implements IdField, StatusFields, CreatedFields {

    public static final String COLLECTION_NAME = "UserAccount";

    public static final String USER_ID = "userId";
    public static final String ACCOUNT_ID = "accountId";

    private UserAccountCollection(){};
}
