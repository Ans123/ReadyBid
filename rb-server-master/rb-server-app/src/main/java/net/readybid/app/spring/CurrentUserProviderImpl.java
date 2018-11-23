package net.readybid.app.spring;

import net.readybid.app.interactors.authentication.user.gate.CurrentUserProvider;
import net.readybid.auth.user.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserProviderImpl implements CurrentUserProvider {
    public AuthenticatedUser get(){
        AuthenticatedUser authenticatedUser = null;
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            authenticatedUser = (AuthenticatedUser) principal;
        }
        return authenticatedUser;
    }
}
