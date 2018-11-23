package net.readybid.api.auth.resetpassword;

import net.readybid.api.auth.web.ResetPasswordRequest;

import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface ResetPasswordService {
    Map<String, Object> resetPasswordAttempt(ResetPasswordRequest resetPasswordRequest);
}
