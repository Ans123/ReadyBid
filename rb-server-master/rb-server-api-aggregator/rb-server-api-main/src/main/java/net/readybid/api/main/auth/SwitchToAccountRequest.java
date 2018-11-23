package net.readybid.api.main.auth;

import net.readybid.validators.Id;

import javax.validation.constraints.NotNull;

public class SwitchToAccountRequest {

    @NotNull
    @Id
    public String accountId;
}
