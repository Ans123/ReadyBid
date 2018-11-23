package net.readybid.mongodb.collections.fields;

import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.List;

import static net.readybid.mongodb.RbMongoFilters.lookup;
import static net.readybid.mongodb.RbMongoFilters.unwind;

public interface CreatedFields {

    String CREATED = "created";
    String CREATED_BY = "created.by";

    List<Bson> CREATED_BY_JOIN = Arrays.asList(
                lookup("User", CREATED_BY, "_id", CREATED_BY),
                unwind("$"+CREATED_BY)
        );
}
