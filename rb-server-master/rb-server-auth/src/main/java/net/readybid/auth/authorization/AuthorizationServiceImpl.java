package net.readybid.auth.authorization;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.AuthenticatedUserRepository;
import net.readybid.auth.user.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by DejanK on 3/23/2017
 *
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final String AUTHORIZATION_HEADER_NAME = "X-AUTH-TOKEN";
    private static final String AUTHORIZATION_HEADER_COOKIE = "Set-Cookie";
    private static final long AUTHORIZATION_TTL = 2419200000L; // 4 weeks
    private static final long AUTHORIZATION_TTL_IN_SECONDS = 2419200L; // 4 weeks
    private static final long MAX_AGE_BEFORE_REFRESH = 3600000L; // 1 hour

    private final AuthorizationTokenService authorizationTokenService;
    private final AuthorizationCookieService authorizationCookieService;
    private final AuthenticatedUserRepository authenticatedUserRepository;

    @Autowired
    public AuthorizationServiceImpl(AuthorizationTokenService authorizationTokenService, AuthenticatedUserRepository authenticatedUserRepository, AuthorizationCookieService authorizationCookieService) {
        this.authorizationTokenService = authorizationTokenService;
        this.authenticatedUserRepository = authenticatedUserRepository;
        this.authorizationCookieService = authorizationCookieService;
    }

    @Override
    public void authenticateResponse(HttpServletResponse httpServletResponse, UserRegistration userRegistration) {
        authenticateResponse(httpServletResponse, userRegistration, false);
    }

    @Override
    public void authenticateResponse(HttpServletResponse httpServletResponse, UserRegistration userRegistration, boolean rememberMe) {
        final AuthorizationTokenClaims claims = new AuthorizationTokenClaims(userRegistration, rememberMe);
        authenticateResponse(httpServletResponse, claims);
    }

    private void authenticateResponse(HttpServletResponse response, AuthorizationTokenClaims claims) {
        response.setHeader(AUTHORIZATION_HEADER_NAME, authorizationTokenService.createHeaderToken(claims, AUTHORIZATION_TTL));
        response.setHeader(AUTHORIZATION_HEADER_COOKIE, authorizationCookieService.createCookie(claims, AUTHORIZATION_TTL, AUTHORIZATION_TTL_IN_SECONDS));
    }

    @Override
    public Authentication authenticateRequest(HttpServletRequest request) {
        AuthenticatedUser auth = null;

        try{
            auth = getRequestAuthentication(request);
        } catch (Exception ignore){
            System.out.println("Authentication Exception");
        }

        return auth;
    }

    private AuthenticatedUser getRequestAuthentication(HttpServletRequest request) {
        final String cookieSessionId = authorizationCookieService.getCookieSessionId(request);
        final String headerToken = request.getHeader(AUTHORIZATION_HEADER_NAME);
        final AuthorizationTokenClaims claims = authorizationTokenService.parseHeaderToken(headerToken);

        return authenticateClaims(claims, cookieSessionId);
    }

    private AuthenticatedUser authenticateClaims(AuthorizationTokenClaims claims, String cookieSessionId) {
        if(claims == null || cookieSessionId == null || cookieSessionId.isEmpty()) return null;
        AuthenticatedUser auth = null;
        if(claims.matches(cookieSessionId)){
            auth = authenticatedUserRepository.get(claims.userId);
            if(auth != null) auth.setCredentials(claims);
        }
        return auth;
    }

    @Override
    public void refreshAuthentication(HttpServletResponse response, AuthenticatedUser authenticatedUser) {
        if(authenticatedUser != null && shouldRefresh(authenticatedUser)){
            final AuthorizationTokenClaims claims = new AuthorizationTokenClaims(authenticatedUser);
            authenticateResponse(response, claims);
        }
    }

    private boolean shouldRefresh(AuthenticatedUser authenticatedUser) {
        final AuthorizationTokenClaims claims = (AuthorizationTokenClaims) authenticatedUser.getCredentials();
        return claims.isOld(MAX_AGE_BEFORE_REFRESH) || authenticatedUser.hasChangedSince(claims.changed);
    }
}
