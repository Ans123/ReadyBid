package net.readybid.validators.captcha;

/**
 * Created by stefan on 10/11/2016.
 */
public interface CaptchaValidationService {
    boolean isValid(String captcha);
}
