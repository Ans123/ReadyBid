package net.readybid.api.auth.web;

import net.readybid.validators.Password;
import net.readybid.validators.RbEmail;
import net.readybid.validators.Text20;
import net.readybid.validators.Text50;

import javax.validation.constraints.NotBlank;

/**
 * Created by DejanK on 10/3/2016.
 *
 */
public class SignUpRequest {

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
}
