package net.readybid.entity_factories;

import net.readybid.utils.CreationDetails;

public class CreationDetailsTestFactory {

    private CreationDetailsTestFactory(){}

    public static CreationDetails random() {
        return new CreationDetails(BasicUserDetailsImplFactory.random());
    }
}
