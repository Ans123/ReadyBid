package net.readybid.auth.authorization;

import net.readybid.auth.cookie.CookieBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
@Service
public class AuthorizationCookieServiceImpl implements AuthorizationCookieService {

    private final String cookieName;
    private final String cookieDomain;


    @Autowired
    public AuthorizationCookieServiceImpl(Environment environment){
        this.cookieDomain = environment.getRequiredProperty("cookie.domain");
        this.cookieName = environment.getRequiredProperty("cookie.name");
    }

    @Override
    public String createCookie(AuthorizationTokenClaims claims, long ttl, long ttlInSeconds) {
        return claims.persistent ? createPersistentCookie(claims.sessionId, ttl, ttlInSeconds) : createSessionCookie(claims.sessionId);
    }

    @Override
    public String getCookieSessionId(HttpServletRequest request) {
        String cookieValue = null;
        final Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies){
                if(cookie.getName().equalsIgnoreCase(cookieName)){
                    cookieValue = cookie.getValue();
                }
            }
        }
        return cookieValue;
    }

    private String createSessionCookie(String sessionId) {
        return createCookie(sessionId)
                .build();
    }

    private String createPersistentCookie(String sessionId, long ttl, long ttlInSeconds) {
        final long expiresAt = new Date().getTime() + ttl;
        return createCookie(sessionId)
                .setMaxAge(ttlInSeconds)
                .setExpires(expiresAt)
                .build();
    }

    private CookieBuilder createCookie(String sessionId){
        final CookieBuilder builder = new CookieBuilder(cookieName, sessionId)
                .setPath("/")
                .setHttpOnly()
                .setSecure();

        if(!(cookieDomain.equals("localhost") || cookieDomain.equalsIgnoreCase("IGNORE"))) {
            // IE ignores localhost
            builder.setDomain(cookieDomain);
        }

        return builder;
    }
}
