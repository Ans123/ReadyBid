package net.readybid.rfp.core;

import net.readybid.validators.Id;

import javax.validation.constraints.NotBlank;

/**
 * Created by DejanK on 9/14/2016.
 *
 */
public class CreateRfpRequest {

    @NotBlank
    @Id
    public String id;
}
