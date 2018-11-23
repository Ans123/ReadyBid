package net.readybid.auth.invitation;

import net.readybid.validators.*;

import javax.validation.constraints.NotBlank;

/**
 * Created by DejanK on 4/12/2017.
 *
 */
public class SignUpWithInvitationRequest {

    @NotBlank
    @Text50
    public String firstName;

    @NotBlank
    @Text50
    public String lastName;

    @Text20
    public String phone;

    @NotBlank
    @RbEmail
    public String emailAddress;

    @NotBlank
    @Password
    public String password;

    @NotBlank
    public String token;

    @NotBlank
    @Text100
    public String jobTitle;
}
