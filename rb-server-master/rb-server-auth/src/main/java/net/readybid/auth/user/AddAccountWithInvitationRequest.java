package net.readybid.auth.user;

import net.readybid.validators.RbEmail;
import net.readybid.validators.Text100;

import javax.validation.constraints.NotBlank;

public class AddAccountWithInvitationRequest {

    @NotBlank
    @RbEmail
    public String emailAddress;

    @NotBlank
    public String token;

    @NotBlank
    @Text100
    public String jobTitle;
}
