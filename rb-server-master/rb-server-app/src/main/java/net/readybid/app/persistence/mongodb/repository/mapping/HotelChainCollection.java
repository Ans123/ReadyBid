package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;

public class HotelChainCollection implements IdField, StatusFields, CreatedFields{

    public static final String COLLECTION_NAME = "Chain";
    public static final String UNVALIDATED_COLLECTION_NAME = "UnvalidatedChain";

    public static final String MASTER_CHAIN_ID = "master._id";
    public static final String MASTER_CHAIN_NAME = "master.name";
}
