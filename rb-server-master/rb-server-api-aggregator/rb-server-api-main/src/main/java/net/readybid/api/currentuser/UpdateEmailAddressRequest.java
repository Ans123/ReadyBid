package net.readybid.api.currentuser;

import net.readybid.validators.RbEmail;

import javax.validation.constraints.NotBlank;

@SuppressWarnings("WeakerAccess")
public class UpdateEmailAddressRequest {

    @NotBlank
    @RbEmail
    public String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }
}
