package net.readybid.app.interactors.core.gate;

import net.readybid.entities.Id;

public interface IdFactory {
    String create();

    Id createId();
}
