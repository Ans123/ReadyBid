package net.readybid.utils;

import net.readybid.test_utils.RbAbstractAssert;

public class CreationDetailsAssert extends RbAbstractAssert<CreationDetailsAssert, CreationDetails> {

    public static CreationDetailsAssert that(CreationDetails actual) {
        return new CreationDetailsAssert(actual, CreationDetailsAssert.class);
    }

    private CreationDetailsAssert(CreationDetails actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public CreationDetailsAssert hasBy(Object expected) {
        assertFieldSame("by", expected, actual.getBy());
        return this;
    }

    public CreationDetailsAssert hasAt () {
        assertFieldNotNull("at", actual.getAt());
        return this;
    }

    public CreationDetailsAssert hasAt (Object expected) {
        assertFieldEquals("at", expected, actual.getAt());
        return this;
    }
}