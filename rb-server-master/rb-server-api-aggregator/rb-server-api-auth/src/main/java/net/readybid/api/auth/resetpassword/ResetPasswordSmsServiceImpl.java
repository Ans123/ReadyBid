package net.readybid.api.auth.resetpassword;

import net.readybid.sms.Sms;
import net.readybid.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class ResetPasswordSmsServiceImpl implements ResetPasswordSmsService {

    private final SmsService smsService;

    @Autowired
    public ResetPasswordSmsServiceImpl(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void sendSms(String phoneNumber, String smsCode) {
        smsService.send(new Sms() {
            @Override
            public String getMessage() {
                return smsCode;
            }

            @Override
            public String getNumber() {
                return phoneNumber;
            }
        });
    }
}
