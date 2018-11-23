package net.readybid.app;

import net.readybid.test_utils.RbAbstractAssert;
import net.readybid.web.RbViewModel;
import org.assertj.core.api.ListAssert;

import java.util.List;
import java.util.function.Consumer;

public class RbViewModelAssert extends RbAbstractAssert<RbViewModelAssert, RbViewModel> {

    public static RbViewModelAssert that(RbViewModel actual) {
        return new RbViewModelAssert(actual);
    }

    private RbViewModelAssert(RbViewModel actual) {
        super(actual, RbViewModelAssert.class);
    }

    public <T> RbViewModelAssert dataAsList(Consumer<ListAssert<T>> consumer) {
        final ListAssert<T> listAssert = new ListAssert<>((List)actual.getData());
        consumer.accept(listAssert);
        return this;}

    public RbViewModelAssert countIs(Object expected) {
        assertFieldEquals("count", expected, actual.getCount());
        return this;
    }

    public RbViewModelAssert hasData(Object expected) {
        assertFieldEquals("data", expected, actual.getData());
        return this;
    }
}
