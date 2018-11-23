package net.readybid.validators.captcha;

import java.util.List;

/**
 * Created by stefan on 10/12/2016.
 */
public class RecaptchaResponse {
    // todo @JsonProperty
    public Boolean success;

    // todo @JsonProperty("error-codes")
    public List<String> errorCodes;
}