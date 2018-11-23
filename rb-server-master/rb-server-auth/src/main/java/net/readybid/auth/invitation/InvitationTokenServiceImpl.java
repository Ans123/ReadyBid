package net.readybid.auth.invitation;

import net.readybid.auth.token.TokenService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * Created by DejanK on 4/12/2017.
 *
 */
@Service
public class InvitationTokenServiceImpl implements InvitationTokenService {

    private final String invitationTokenKey;
    private final TokenService tokenService;

    public InvitationTokenServiceImpl(Environment environment, TokenService tokenService) {
        this.invitationTokenKey = environment.getRequiredProperty("token-key.invitation");
        this.tokenService = tokenService;
    }

    @Override
    public String createToken(Invitation invitation) {
        final Date expiresAt = invitation.getExpiryDate();
        return tokenService.createToken(invitationTokenKey, Collections.singletonMap("id", invitation.getId().toString()), expiresAt);
    }

    @Override
    public Map<String, Object> parseToken(String token) {
        return tokenService.parseToken(invitationTokenKey, token);
    }
}
