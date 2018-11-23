package net.readybid.api.auth.resetpassword;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface ResetPasswordSmsService {
    void sendSms(String phoneNumber, String smsCode);
}
