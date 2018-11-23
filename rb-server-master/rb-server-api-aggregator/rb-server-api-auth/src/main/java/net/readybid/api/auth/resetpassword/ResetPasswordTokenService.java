package net.readybid.api.auth.resetpassword;

import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface ResetPasswordTokenService {
    String ID_FIELD = "id";
    String STATE_FIELD = "state";

    String createToken(ResetPasswordAttempt resetPasswordAttempt, long expiresAt);

    Map<String, Object> parseToken(String token);
}
