package net.readybid.auth.useraccount.web;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.validators.Id;
import net.readybid.validators.Text100;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by DejanK on 11/11/2016.
 *
 */
public class AddUserAccountRequest {

    @NotBlank
    @Id
    public String entityId;

    @NotNull
    @Valid
    public EntityType entityType;

    @NotBlank
    @Text100
    public String jobTitle;
}
