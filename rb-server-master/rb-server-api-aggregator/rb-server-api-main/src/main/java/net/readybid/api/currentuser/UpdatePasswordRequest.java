package net.readybid.api.currentuser;

import net.readybid.validators.Password;

import javax.validation.constraints.NotBlank;

public class UpdatePasswordRequest {
    @NotBlank
    @Password
    public String password;

    public String getPassword() {
        return password;
    }
}
