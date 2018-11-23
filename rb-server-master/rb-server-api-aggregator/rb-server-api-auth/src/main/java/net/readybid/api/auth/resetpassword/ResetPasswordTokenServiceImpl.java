package net.readybid.api.auth.resetpassword;

import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.auth.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class ResetPasswordTokenServiceImpl implements ResetPasswordTokenService {

    private final String tokenKey;
    private final TokenService tokenService;

    @Autowired
    public ResetPasswordTokenServiceImpl(Environment env, TokenService tokenService) {
        tokenKey = env.getRequiredProperty("token-key.forgotten-password.email");
        this.tokenService = tokenService;
    }

    @Override
    public String createToken(ResetPasswordAttempt resetPasswordAttempt, long expiresAt) {
        final HashMap<String, Object> payload = new HashMap<>();
        final Date issueDate = new Date();
        final Date expiryDate = new Date(expiresAt);

        payload.put(ID_FIELD, resetPasswordAttempt.getId().toString());
        payload.put(STATE_FIELD, resetPasswordAttempt.getState());

        return tokenService.createToken(tokenKey, payload, issueDate, expiryDate);
    }

    @Override
    public Map<String, Object> parseToken(String token) {
        final Map<String, Object> payload = tokenService.parseToken(tokenKey, token);
        if (payload == null || !payload.containsKey(ID_FIELD) || !payload.containsKey(STATE_FIELD)) throw new NotFoundException();
        return payload;
    }
}
