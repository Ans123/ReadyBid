package net.readybid.utils;

import net.readybid.test_utils.RbAbstractAssert;

public class StatusDetailsAssert extends RbAbstractAssert<StatusDetailsAssert, StatusDetails> {

    public static StatusDetailsAssert that(StatusDetails actual) {
        return new StatusDetailsAssert(actual, StatusDetailsAssert.class);
    }

    private StatusDetailsAssert(StatusDetails actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public StatusDetailsAssert hasBy(Object expected) {
        assertFieldSame("by", expected, actual.getBy());
        return this;
    }

    public StatusDetailsAssert hasValue(Object expected) {
        assertFieldEquals("value", expected, actual.getValue());
        return this;
    }

    public StatusDetailsAssert hasAt () {
        assertFieldNotNull("at", actual.getAt());
        return this;
    }

    public StatusDetailsAssert hasAt (Object expected) {
        assertFieldEquals("at", expected, actual.getAt());
        return this;
    }
}