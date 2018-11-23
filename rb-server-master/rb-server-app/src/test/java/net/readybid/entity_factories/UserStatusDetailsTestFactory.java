package net.readybid.entity_factories;

import net.readybid.auth.user.UserStatus;
import net.readybid.auth.user.UserStatusDetails;
import net.readybid.test_utils.RbRandom;

public class UserStatusDetailsTestFactory {

    private UserStatusDetailsTestFactory(){}

    public static UserStatusDetails random() {

        final UserStatusDetails statusDetails = new UserStatusDetails();

        statusDetails.setAt(RbRandom.date());
        statusDetails.setBy(BasicUserDetailsImplFactory.random());
        statusDetails.setValue(RbRandom.randomEnum(UserStatus.class));

        return statusDetails;
    }
}
