package net.readybid.test_utils;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.MapAssert;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class RbAbstractAssert<S extends RbAbstractAssert<S, A>, A> extends AbstractAssert<S, A>  {

    public RbAbstractAssert(A actual, Class<?> selfType) {
        super(actual, selfType);
    }

    protected final void assertFieldEquals(final String fieldName, final Object expected, final Object actual){
        isNotNull();
        if (!Objects.equals(actual, expected)) {
            failWithMessage("Expected %s to be <%s> but was <%s>", fieldName, expected, actual);
        }
    }

    protected final void assertFieldSame(final String fieldName, final Object expected, final Object actual){
        isNotNull();
        if (actual != expected) {
            failWithMessage("Expected %s to be same", fieldName);
        }
    }

    protected final void assertFieldNotNull(final String fieldName, final Object actual) {
        isNotNull();
        if (actual == null) {
            failWithMessage("Expected %s Not to be Null", fieldName);
        }
    }

    protected final void assertFieldNull(final String fieldName, final Object actual) {
        isNotNull();
        if (actual != null) {
            failWithMessage("Expected %s Not to be Null", fieldName);
        }
    }

    protected final void assertAfterInclusive(final String fieldName, final ZonedDateTime expected, final ZonedDateTime actual) {
        isNotNull();
        if (actual.isBefore(expected)) {
            failWithMessage("Expected %s not to be before <%tY-%<tm-%<td %<tH:%<tM:%<tS.%<tL%<tz> but was <%tY-%<tm-%<td %<tH:%<tM:%<tS.%<tL%<tz>", fieldName, expected, actual);
        }
    }

    protected void assertMap(Map<String, Object> map, Consumer<RbMapAssert<String, Object>> that){
        that.accept(RbMapAssert.that(map));
    }
}