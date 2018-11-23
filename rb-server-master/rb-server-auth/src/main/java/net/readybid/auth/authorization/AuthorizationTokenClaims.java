package net.readybid.auth.authorization;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.User;
import net.readybid.auth.user.UserStatus;
import net.readybid.exceptions.BadRequestException;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 3/23/2017.
 *
 */
public class AuthorizationTokenClaims {

    public static final String USER_ID_FIELD = "userId";
    public static final String STATUS_FIELD = "status";
    public static final String SESSION_ID_FIELD = "sessionId";
    public static final String PERSISTENT_FIELD = "persistent";
    public static final String CHANGED_AT_FIELD = "changed";
    public static final String ISSUED_AT_FIELD = "issued";

    private static SecureRandom SECURE_RANDOM = new SecureRandom();

    private static String generateRandomString(){
        return new BigInteger(130, SECURE_RANDOM).toString(32);
    }

    public final ObjectId userId;
    public final String sessionId;
    public final UserStatus status;
    public final boolean persistent;
    public final long changed;
    public final long iat;

    public AuthorizationTokenClaims(User userRegistration, boolean rememberMe) {
        sessionId = generateRandomString();
        userId = userRegistration.getId();
        status = userRegistration.getStatusValue();
        persistent = rememberMe;
        changed = userRegistration.getLastChangeTimestamp();
        iat = new Date().getTime();
    }

    public AuthorizationTokenClaims(Authentication authentication) {
        final AuthorizationTokenClaims claims = (AuthorizationTokenClaims) authentication.getCredentials();
        final AuthenticatedUser user = (AuthenticatedUser) authentication;

        sessionId = claims.sessionId;
        userId = claims.userId;
        persistent = claims.persistent;
        status = user.getStatusValue();
        changed = user.getLatestChangeTimestamp();
        iat = new Date().getTime();
    }

    private AuthorizationTokenClaims(Map<String, Object> claimsMap) {
        try{
            userId = new ObjectId(String.valueOf(claimsMap.get(USER_ID_FIELD)));
            status = UserStatus.valueOf(String.valueOf(claimsMap.get(STATUS_FIELD)));
            sessionId = String.valueOf(claimsMap.get(SESSION_ID_FIELD));
            persistent = Boolean.valueOf(String.valueOf(claimsMap.get(PERSISTENT_FIELD)));
            changed = Long.valueOf(String.valueOf(claimsMap.get(CHANGED_AT_FIELD)));
            iat = Long.valueOf(String.valueOf(claimsMap.get(ISSUED_AT_FIELD)));
        } catch (Exception e){
            System.out.println("- Authorization Token Claims creation failed");
            e.printStackTrace();
            throw new BadRequestException("INVALID_AUTHORIZATION_TOKEN");
        }
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(USER_ID_FIELD, String.valueOf(userId));
        claimsMap.put(STATUS_FIELD, String.valueOf(status));
        claimsMap.put(SESSION_ID_FIELD, String.valueOf(sessionId));
        claimsMap.put(PERSISTENT_FIELD, String.valueOf(persistent));
        claimsMap.put(CHANGED_AT_FIELD, String.valueOf(changed));
        claimsMap.put(ISSUED_AT_FIELD, String.valueOf(iat));
        return claimsMap;
    }

    public static AuthorizationTokenClaims fromMap(Map<String, Object> claimsMap) {
        return isValid(claimsMap) ? new AuthorizationTokenClaims(claimsMap) : null;
    }

    private static boolean isValid(Map<String, Object> claimsMap) {
        return claimsMap != null
                && claimsMap.containsKey(USER_ID_FIELD)
                && claimsMap.containsKey(STATUS_FIELD)
                && claimsMap.containsKey(SESSION_ID_FIELD)
                && claimsMap.containsKey(PERSISTENT_FIELD)
                && claimsMap.containsKey(CHANGED_AT_FIELD)
                && claimsMap.containsKey(ISSUED_AT_FIELD);
    }

    public boolean matches(String cookieSessionId) {
        return sessionId != null && sessionId.equals(cookieSessionId);
    }

    public boolean isOld(long maxAge) {
        return iat + maxAge < new Date().getTime();
    }
}
