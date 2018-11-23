package net.readybid.app.entities.core;


import net.readybid.test_utils.RbAbstractAssert;

public class ActionReportAssert extends RbAbstractAssert<ActionReportAssert, ActionReport> {

    public static ActionReportAssert that(ActionReport actual) {
        return new ActionReportAssert(actual);
    }

    private ActionReportAssert(ActionReport actual) {
        super(actual, ActionReportAssert.class);
    }

    public ActionReportAssert hasStatus(Object expected) {
        assertFieldEquals("status", String.valueOf(expected), String.valueOf(actual.status));
        return this;
    }

    public ActionReportAssert hasMessage(Object expected) {
        assertFieldEquals("message", expected, actual.message);
        return this;
    }

    public ActionReportAssert hasObject(Object expected) {
        assertFieldEquals("object", expected, actual.tObject);
        return this;
    }

}