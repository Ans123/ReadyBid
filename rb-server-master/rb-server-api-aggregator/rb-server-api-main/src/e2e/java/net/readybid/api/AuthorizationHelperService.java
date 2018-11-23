package net.readybid.api;

import com.mongodb.client.MongoDatabase;
import net.readybid.auth.account.core.AccountImpl;
import net.readybid.auth.authorization.AuthorizationCookieService;
import net.readybid.auth.authorization.AuthorizationTokenClaims;
import net.readybid.auth.authorization.AuthorizationTokenService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.AuthenticatedUserImpl;
import net.readybid.auth.useraccount.core.UserAccountImpl;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Profile("e2eTest")
@Service
public class AuthorizationHelperService {

    private static final String AUTHORIZATION_HEADER_NAME = "X-AUTH-TOKEN";
    private static final String AUTHORIZATION_HEADER_COOKIE = "Set-Cookie";
    private static final long AUTHORIZATION_TTL = 2419200000L; // 4 weeks
    private static final long AUTHORIZATION_TTL_IN_SECONDS = 2419200L; // 4 weeks

    private final AuthorizationTokenService authorizationTokenService;
    private final AuthorizationCookieService authorizationCookieService;
    private final MongoDatabase mongoDatabase;


    public AuthorizationHelperService(
            AuthorizationTokenService authorizationTokenService,
            AuthorizationCookieService authorizationCookieService,
            MongoDatabase mongoDatabase
    ) {
        this.authorizationTokenService = authorizationTokenService;
        this.authorizationCookieService = authorizationCookieService;
        this.mongoDatabase = mongoDatabase;
    }


    public void authorize(HttpHeaders headers, AuthenticatedUser user) {
        mongoDatabase.getCollection("Account", AccountImpl.class).insertOne((AccountImpl) user.getAccount());
        mongoDatabase.getCollection("UserAccount", UserAccountImpl.class).insertOne((UserAccountImpl) user.getCurrentUserAccount());
        mongoDatabase.getCollection("User", AuthenticatedUserImpl.class).insertOne((AuthenticatedUserImpl) user);

        final AuthorizationTokenClaims claims = new AuthorizationTokenClaims(user, false);
        headers.add(AUTHORIZATION_HEADER_NAME, authorizationTokenService.createHeaderToken(claims, AUTHORIZATION_TTL));
        headers.add("Cookie", authorizationCookieService.createCookie(claims, AUTHORIZATION_TTL, AUTHORIZATION_TTL_IN_SECONDS));
    }
}
