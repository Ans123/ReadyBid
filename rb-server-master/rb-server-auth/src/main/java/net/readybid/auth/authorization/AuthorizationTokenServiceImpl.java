package net.readybid.auth.authorization;

import net.readybid.auth.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by DejanK on 3/23/2017.
 *
 */
@Service
public class AuthorizationTokenServiceImpl implements AuthorizationTokenService {

    private final TokenService tokenService;
    private final String authorizationHeaderTokenKey;

    @Autowired
    public AuthorizationTokenServiceImpl(TokenService tokenService, Environment environment) {
        this.tokenService = tokenService;
        this.authorizationHeaderTokenKey = environment.getRequiredProperty("token-key.auth-header");
    }

    @Override
    public String createHeaderToken(AuthorizationTokenClaims claims, long ttl) {
        final Date issuedAt = new Date();
        final Date expiresAt = new Date(issuedAt.getTime() + ttl);
        return tokenService.createToken(authorizationHeaderTokenKey, claims.toMap(), issuedAt, expiresAt);
    }

    @Override
    public AuthorizationTokenClaims parseHeaderToken(String headerToken) {
        if (headerToken == null) return null;
        final Map<String, Object> claimsMap = tokenService.parseToken(authorizationHeaderTokenKey, headerToken);
        return AuthorizationTokenClaims.fromMap(claimsMap);
    }
}
