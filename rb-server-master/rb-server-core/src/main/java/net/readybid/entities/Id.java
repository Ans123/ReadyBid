package net.readybid.entities;

import java.util.List;
import java.util.stream.Collectors;

/**
 * All Ids should ideally be converted to Id. That's a huge task so for now we can use it for static methods
 */
public class Id {

    public final String value;

    public Id(String value) {
        this.value = value;
    }

    public static List<Id> asList(List<String> ids) {
        return ids.stream().map(Id::new).collect(Collectors.toList());
    }

    public static boolean areEqual(Object a, Object b){
        return String.valueOf(a).equals(String.valueOf(b));
    }

    public static Id valueOf(Object id) {
        return new Id(asString(id));
    }

    public static String asString(Object id) {
        return String.valueOf(id);
    }

    @Override
    public String toString(){
        return value;
    }


    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }

    /**
     * In lots of places id field is using MongoDB ObjectId.
     * ObjectId is in process of being removed from outside Persistence package.
     * Until that is completed, equality is checked with String comparison
     */
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return areEqual(this, obj);
    }
}
