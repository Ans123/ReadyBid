package net.readybid.auth.authorization;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.UserRegistration;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by DejanK on 3/23/2017.
 *
 */
public interface AuthorizationService {
    void authenticateResponse(HttpServletResponse httpServletResponse, UserRegistration userRegistration);

    void authenticateResponse(HttpServletResponse httpServletResponse, UserRegistration userRegistration, boolean rememberMe);

    Authentication authenticateRequest(HttpServletRequest request);

    void refreshAuthentication(HttpServletResponse response, AuthenticatedUser authentication);
}
