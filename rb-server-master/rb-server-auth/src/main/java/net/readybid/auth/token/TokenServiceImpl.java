package net.readybid.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 10/4/2016.
 *
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String createToken(String tokenKey, Map<String, Object> payload, Date issuedAt, Date expireAt) {
        return Jwts.builder()
                .setClaims(new HashMap<>(payload))
                .setIssuedAt(issuedAt)
                .setExpiration(expireAt)
                .signWith(SignatureAlgorithm.HS512, tokenKey)
                .compact();
    }

    @Override
    public String createToken(String tokenKey, Map<String, Object> payload, long ttl) {
        final Date issuedAt = new Date();
        final Date expiresAt = new Date(issuedAt.getTime() + ttl);
        return createToken(tokenKey, payload, issuedAt, expiresAt);
    }

    @Override
    public String createToken(String tokenKey, Map<String, Object> payload, Date expiresAt) {
        final Date issuedAt = new Date();
        return createToken(tokenKey, payload, issuedAt, expiresAt);
    }

    @Override
    public Map<String, Object> parseToken(String tokenKey, String token){
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(tokenKey)
                    .parseClaimsJws(token)
                    .getBody();
            return convertToMap(claims);
        } catch (Exception e){
            System.out.println("--- Token Check Exception");
            e.printStackTrace();
            System.out.println("--- Token Check Exception");
            return null;
        }
    }

    private Map<String, Object> convertToMap(Claims claims) {
        final HashMap<String, Object> claimsMap = new HashMap<>();
        for(Map.Entry<String, Object> entry : claims.entrySet()){
            claimsMap.put(entry.getKey(), entry.getValue());
        }
        return claimsMap;
    }
}
