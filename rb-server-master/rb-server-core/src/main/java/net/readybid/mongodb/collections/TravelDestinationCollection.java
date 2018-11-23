package net.readybid.mongodb.collections;

import net.readybid.mongodb.collections.fields.CreatedFields;
import net.readybid.mongodb.collections.fields.IdField;
import net.readybid.mongodb.collections.fields.StatusFields;

public class TravelDestinationCollection implements IdField, StatusFields, CreatedFields{

    public static final String COLLECTION_NAME = "TravelDestination";
}
