package net.readybid.api.auth.registration;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.auth.token.TokenService;
import net.readybid.auth.user.UserStatusDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class EmailAddressVerificationServiceImpl implements EmailAddressVerificationService {

    private final String tokenKey;
    private final TokenService tokenService;
    private static final long TTL = 172800000L; // 2 days: 48*60*60*1000
    private static final int TTL_HOURS = 48; // 2 days: 48

    @Autowired
    public EmailAddressVerificationServiceImpl(
            @Value("${token-key.email-verification}") String tokenKey,
            TokenService tokenService
    ) {
        this.tokenService = tokenService;
        this.tokenKey = tokenKey;
    }

    @Override
    public String createToken(ObjectId userId, String emailAddress) {
        final HashMap<String, Object> payload = new HashMap<>();
        payload.put("email", emailAddress);
        payload.put("id", userId.toString());
        final Date issueDate = new Date();
        final Date expiryDate = new Date(issueDate.getTime() + TTL);
        return tokenService.createToken(tokenKey, payload, issueDate, expiryDate);
    }

    @Override
    public int getTimeToLiveInHours() {
        return TTL_HOURS;
    }

    @Override
    public Map<String, Object> parseToken(String token) {
        final Map<String, Object> payload = tokenService.parseToken(tokenKey, token);
        if(payload == null || !payload.containsKey("email")|| !payload.containsKey("id")) throw new NotFoundException();
        return payload;
    }

    @Override
    public boolean isEmailAddressVerificationExpired(UserStatusDetails status) {
        return status == null || new Date().getTime() - status.getAt().getTime() > TTL;
    }
}
