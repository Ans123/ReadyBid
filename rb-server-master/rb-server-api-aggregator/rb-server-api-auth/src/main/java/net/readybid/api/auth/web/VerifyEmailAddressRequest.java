package net.readybid.api.auth.web;


import javax.validation.constraints.NotBlank;

/**
 * Created by DejanK on 10/14/2016.
 *
 */
public class VerifyEmailAddressRequest {

    @NotBlank
    public String token;
}
