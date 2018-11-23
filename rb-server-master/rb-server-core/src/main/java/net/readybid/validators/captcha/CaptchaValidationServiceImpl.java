package net.readybid.validators.captcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by stefan on 10/11/2016.
 */

@Service
public class CaptchaValidationServiceImpl implements CaptchaValidationService {
    private static final String URL = "https://www.google.com/recaptcha/api/siteverify";
    private final String secret;

    public CaptchaValidationServiceImpl(@Value("${com.google.recaptcha.secret}") String secret){
        this.secret = secret;
    }
    @Override
    public boolean isValid(String captcha) {
        if (captcha == null || captcha.isEmpty()) {
            return false;
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", secret);
        map.add("response", captcha);

        RestTemplate restTemplate = new RestTemplate();
        RecaptchaResponse response = restTemplate.postForObject(URL, map, RecaptchaResponse.class);
        return response != null && response.success;
    }
}
