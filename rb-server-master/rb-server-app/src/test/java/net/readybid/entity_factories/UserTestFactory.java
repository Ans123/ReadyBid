package net.readybid.entity_factories;

import net.readybid.auth.user.UserImpl;
import net.readybid.test_utils.RbRandom;

public class UserTestFactory {

    public static UserImpl random() {

        final UserImpl user = new UserImpl();

        user.setId(RbRandom.oid());
        user.setFirstName(RbRandom.name());
        user.setLastName(RbRandom.name());
        user.setEmailAddress(RbRandom.emailAddress());
        user.setPhone(RbRandom.phone());
        user.setProfilePicture(RbRandom.alphanumeric(100, false));

        user.setCreated(CreationDetailsTestFactory.random());
        user.setStatus(UserStatusDetailsTestFactory.random());

        return user;
    }
}
