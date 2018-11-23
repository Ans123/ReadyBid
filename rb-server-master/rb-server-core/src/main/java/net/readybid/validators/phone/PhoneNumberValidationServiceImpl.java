package net.readybid.validators.phone;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import net.readybid.validators.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by DejanK on 10/7/2016.
 *
 */
@Service
public class PhoneNumberValidationServiceImpl implements ConstraintValidator<PhoneNumber, String>, PhoneNumberValidationService {

    private final PhoneNumberUtil phoneNumberUtil;

    @Autowired
    public PhoneNumberValidationServiceImpl(PhoneNumberUtil phoneNumberUtil) {
        this.phoneNumberUtil = phoneNumberUtil;
    }

    @Override
    public boolean isValid(String phoneNumber){
        try {
            final Phonenumber.PhoneNumber phNumberProto = phoneNumberUtil.parse(phoneNumber, "US");
            return phoneNumberUtil.isValidNumber(phNumberProto);
        } catch (NumberParseException e) {
            return false;
        }
    }

    @Override
    public void initialize(PhoneNumber mobilePhoneNumber) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || isValid(s);
    }
}
