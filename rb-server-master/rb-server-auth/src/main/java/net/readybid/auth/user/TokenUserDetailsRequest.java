package net.readybid.auth.user;


import javax.validation.constraints.NotBlank;

/**
 * Created by DejanK on 5/20/2017.
 *
 */
public class TokenUserDetailsRequest {

    @NotBlank
    public String token;
}
