package net.readybid.test_utils;

import org.assertj.core.api.MapAssert;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RbMapAssert<K, V> extends MapAssert<K, V> {

    public static <K, V> RbMapAssert<K, V> that(Map<K, V> actual){
        return new RbMapAssert<>(actual);
    }

    private RbMapAssert(Map<K, V> actual) {
        super(actual);
    }

    public RbMapAssert<K, V> contains(K key, V value){
        super.containsEntry(key, value);
        return this;
    }

    public RbMapAssert<K, V> hasAfter(K key, ZonedDateTime date) {
        return hasAfter(key, date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public RbMapAssert<K, V> hasAfter(K key, ZonedDateTime expectedDate, DateTimeFormatter formatter) {
        isNotNull();
        final Object actualDateObject = actual.get(key);
        ZonedDateTime actualDate = null;

        if(actualDateObject == null){
            failWithMessage("Value of field <%s> is not Null", key);
        } else if(actualDateObject instanceof ZonedDateTime) {
            actualDate = (ZonedDateTime) actualDateObject;
        } else if(actualDateObject instanceof String){
            actualDate = ZonedDateTime.parse((String) actualDateObject, formatter);
        } else {
            failWithMessage("Value of field <%s> is not a ZonedDateTime nor String but <%s>", key);
        }

        // this is necessary to avoid differences in nano seconds if they are lost in format
        final ZonedDateTime parsedExpectedDate = ZonedDateTime.from(formatter.parse(formatter.format(expectedDate)));
        if(actualDate != null && actualDate.isBefore(parsedExpectedDate)){
            failWithMessage("Value of field <%s> should be after <%s> but was <%s>", key, expectedDate, actualDate);
        }

        return this;
    }
}
