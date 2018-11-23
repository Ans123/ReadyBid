package net.readybid.entity_factories;

import net.readybid.test_utils.RbRandom;
import net.readybid.user.BasicUserDetailsImpl;

public class BasicUserDetailsImplFactory {

    private BasicUserDetailsImplFactory(){}

    public static BasicUserDetailsImpl random() {

        final BasicUserDetailsImpl user = new BasicUserDetailsImpl();

        user.setId(RbRandom.oid());
        user.setFirstName(RbRandom.name());
        user.setLastName(RbRandom.name());
        user.setEmailAddress(RbRandom.emailAddress());
        user.setPhone(RbRandom.phone());

        return user;
    }
}
