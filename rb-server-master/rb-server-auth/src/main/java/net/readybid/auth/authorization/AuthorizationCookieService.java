package net.readybid.auth.authorization;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
public interface AuthorizationCookieService {

    String createCookie(AuthorizationTokenClaims claims, long ttl, long ttlInSeconds);

    String getCookieSessionId(HttpServletRequest request);
}
