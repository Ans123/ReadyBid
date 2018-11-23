package net.readybid.auth.useraccount.web;

import net.readybid.entities.core.CreateEntityRequest;
import net.readybid.validators.Text100;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by DejanK on 11/11/2016.
 *
 */
public class CreateAccountRequest {

    @NotNull
    @Valid
    public CreateEntityRequest entity;

    @NotBlank
    @Text100
    public String jobTitle;
}
