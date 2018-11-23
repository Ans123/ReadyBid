package net.readybid.api.auth.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by stefan on 10/10/2016.
 *
 */
public class SignInRequest {

    @NotNull
    @Size(max=1000)
    public String email;

    @NotNull
    @Size(max=1000)
    public String password;

    public boolean rememberMe;

    @Size(max=5000)
    public String captcha;
}
