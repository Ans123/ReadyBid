package net.readybid.auth.authorization;

/**
 * Created by DejanK on 3/23/2017.
 *
 */
public interface AuthorizationTokenService {
    String createHeaderToken(AuthorizationTokenClaims claims, long ttl);

    AuthorizationTokenClaims parseHeaderToken(String headerToken);
}
