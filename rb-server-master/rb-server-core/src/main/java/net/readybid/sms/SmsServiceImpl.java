package net.readybid.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import net.readybid.exceptions.UnableToCompleteRequestException;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 11/23/2016.
 *
 */
@Service
public class SmsServiceImpl implements SmsService {

    public static final String ACCOUNT_SID = "AC4501a52b558931b2dff20b21731a091c";
    public static final String AUTH_TOKEN = "04ae197036113b5c828e707d2b1fc5b2";
    public static final String MESSAGING_SERVICE_ID = "MG0f0c40b7be0a06b56c1d2c6c0f1ecf5c";


    public SmsServiceImpl() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Override
    public void send(Sms sms) {
        try {
            preSendingCheck(sms.getNumber(), sms.getMessage());
            Message.creator(new PhoneNumber(sms.getNumber()), MESSAGING_SERVICE_ID, sms.getMessage())
                    .create();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new UnableToCompleteRequestException("SMS Sending Error");
        }
    }

    private void preSendingCheck(String mobile, String messageBody) {
        if(mobile == null || mobile.isEmpty()){
            throw new UnableToCompleteRequestException("NO_PHONE");
        }
        if(messageBody == null || messageBody.isEmpty()){
            throw new UnableToCompleteRequestException("Invalid sms body");
        }
    }
}
