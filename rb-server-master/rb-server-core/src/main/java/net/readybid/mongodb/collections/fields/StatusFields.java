package net.readybid.mongodb.collections.fields;

import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.List;

import static net.readybid.mongodb.RbMongoFilters.lookup;
import static net.readybid.mongodb.RbMongoFilters.unwind;

public interface StatusFields {
    String STATUS_BY = "status.by";
    List<Bson> STATUS_BY_JOIN = Arrays.asList(
            lookup("User", STATUS_BY, "_id", STATUS_BY),
            unwind("$"+ STATUS_BY));
}
