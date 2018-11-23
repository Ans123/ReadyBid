package net.readybid.auth.invitation;

import java.util.Map;

/**
 * Created by DejanK on 4/12/2017.
 *
 */
public interface InvitationTokenService {
    Map<String, Object> parseToken(String token);

    String createToken(Invitation invitation);
}
