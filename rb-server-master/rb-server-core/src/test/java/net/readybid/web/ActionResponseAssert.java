package net.readybid.web;

import org.assertj.core.api.AbstractAssert;

import java.util.Objects;

/**
 * Created by DejanK on 11/30/2016.
 *
 */
public class ActionResponseAssert extends AbstractAssert<ActionResponseAssert, ActionResponse> {

    public static ActionResponseAssert that(ActionResponse actual) {
        return new ActionResponseAssert(actual, ActionResponseAssert.class);
    }

    private ActionResponseAssert(ActionResponse actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public ActionResponseAssert hasTime() {
        isNotNull();
        if (Objects.isNull(actual.time)) {
            failWithMessage("Expected Action Result time to be defined");
        }
        return this;
    }

    public ActionResponseAssert hasOkStatus() {
        isNotNull();
        if (!Objects.equals(actual.status, "OK")) {
            failWithMessage("Expected Action Result status to be <%s> but was <%s>", "OK", actual.status);
        }
        return this;
    }


}
