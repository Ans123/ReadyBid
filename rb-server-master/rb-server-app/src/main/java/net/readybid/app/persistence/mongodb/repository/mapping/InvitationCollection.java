package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;

public class InvitationCollection implements IdField, StatusFields, CreatedFields {

    public static final String COLLECTION_NAME = "Invitation";

    public static final String ACCOUNT_ID = "accountId";
    public static final String ACCOUNT_NAME = "accountName";
    public static final String TARGET_ID = "targetId";
}
