package net.readybid.entity_factories;

import net.readybid.auth.useraccount.UserAccountStatus;
import net.readybid.auth.useraccount.UserAccountStatusDetails;
import net.readybid.test_utils.RbRandom;

public class UserAccountStatusDetailsTestFactory {

    private UserAccountStatusDetailsTestFactory(){}

    public static UserAccountStatusDetails random() {

        final UserAccountStatusDetails statusDetails = new UserAccountStatusDetails();

        statusDetails.setAt(RbRandom.date());
        statusDetails.setBy(BasicUserDetailsImplFactory.random());
        statusDetails.setValue(RbRandom.randomEnum(UserAccountStatus.class));

        return statusDetails;
    }
}
