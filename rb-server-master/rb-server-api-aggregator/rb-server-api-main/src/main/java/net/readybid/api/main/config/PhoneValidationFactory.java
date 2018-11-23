package net.readybid.api.main.config;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by DejanK on 10/7/2016.
 *
 */
@Configuration
public class PhoneValidationFactory {

    @Bean
    public PhoneNumberUtil getPhoneNumberUtil(){
        return PhoneNumberUtil.getInstance();
    }
}
