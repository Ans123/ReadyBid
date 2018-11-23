package net.readybid.auth.token;

import java.util.Date;
import java.util.Map;

/**
 * Created by DejanK on 10/4/2016.
 *
 */
public interface TokenService {
    String createToken(String tokenKey, Map<String, Object> payload, Date issuedAt, Date expireAt);

    String createToken(String tokenKey, Map<String, Object> payload, Date expireAt);

    String createToken(String tokenKey, Map<String, Object> payload, long ttl);

    Map<String, Object> parseToken(String tokenKey, String token);


}
