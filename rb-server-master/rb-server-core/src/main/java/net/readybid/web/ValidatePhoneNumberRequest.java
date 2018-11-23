package net.readybid.web;

import net.readybid.validators.PhoneNumber;

import javax.validation.constraints.NotBlank;

/**
 * Created by stefan on 10/7/2016.
 *
 */
public class ValidatePhoneNumberRequest {

    @NotBlank
    @PhoneNumber
    public String number;
}
