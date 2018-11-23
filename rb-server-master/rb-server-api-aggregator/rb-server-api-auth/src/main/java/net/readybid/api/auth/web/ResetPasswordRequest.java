package net.readybid.api.auth.web;

import net.readybid.validators.Password;
import net.readybid.validators.RbEmail;

/**
 * Created by DejanK on 11/22/2016.
 *
 */
public class ResetPasswordRequest {

    @RbEmail
    public String emailAddress;

    public String token;

    public String code;

    @Password
    public String password;
}
