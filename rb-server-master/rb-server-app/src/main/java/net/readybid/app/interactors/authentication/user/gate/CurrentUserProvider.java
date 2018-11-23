package net.readybid.app.interactors.authentication.user.gate;

import net.readybid.auth.user.AuthenticatedUser;

public interface CurrentUserProvider {
    AuthenticatedUser get();
}
