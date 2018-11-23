package net.readybid.app.persistence.mongodb.repository.mapping;

import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;

public class HotelCollection implements IdField, StatusFields, CreatedFields {

    public static final String COLLECTION_NAME = "Hotel";
    public static final String UNVALIDATED_COLLECTION_NAME = "UnvalidatedHotel";

    public static final String CHAIN_ID = "chain._id";
    public static final String CHAIN_NAME = "chain.name";
    public static final String MASTER_CHAIN_ID = "chain.master._id";
    public static final String MASTER_CHAIN_NAME = "chain.master.name";
    public static final String ANSWERS = "answers";

    public static final String PROPCODE = "answers.PROPCODE";
    public static final String INTERNALHOTELCODE = "answers.INTERNALHOTELCODE";
    public static final String SABRE_PROPCODE = "answers.SABRE_PROPCODE";
    public static final String AMADEUS_PROPCODE = "answers.AMADEUS_PROPCODE";
    public static final String APOLLO_PROPCODE = "answers.APOLLO_PROPCODE";
    public static final String WRLDSPAN_PROPCODE = "answers.WRLDSPAN_PROPCODE";
}