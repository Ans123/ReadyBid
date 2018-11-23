package net.readybid.api.auth.resetpassword;

import org.bson.types.ObjectId;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public class ResetPasswordAttemptImpl implements ResetPasswordAttempt{

    private ObjectId id;
    private ObjectId userId;
    private String userName;
    private String emailAddress;
    private String phoneNumber;
    private ResetPasswordAttemptState state;
    private String smsCode;
    private long expiresAt;
    private int smsCodeAttemptsCount;

    public ResetPasswordAttemptImpl() {}

    public ResetPasswordAttemptImpl(ResetPasswordAttempt attempt) {
        setId(attempt.getId());
        setUserId(attempt.getUserId());
        setUserName(attempt.getUserName());
        setEmailAddress(attempt.getEmailAddress());
        setPhoneNumber(attempt.getPhoneNumber());
        setState(attempt.getState());
    }


    @Override
    public boolean hasExpired() {
        return new Date().getTime() > expiresAt;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean shouldVerifyEmailAddress() {
        return ResetPasswordAttemptState.VERIFY_EMAIL_ADDRESS.equals(state);
    }

    @Override
    public boolean shouldVerifyMobilePhone() {
        return ResetPasswordAttemptState.VERIFY_MOBILE_PHONE.equals(state);
    }

    @Override
    public int getSmsCodeAttemptsCount() {
        return smsCodeAttemptsCount;
    }

    @Override
    public boolean shouldSetPassword() {
        return ResetPasswordAttemptState.SET_PASSWORD.equals(state);
    }

    @Override
    public ObjectId getUserId() {
        return userId;
    }

    @Override
    public String getSmsCode() {
        return smsCode;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public ResetPasswordAttemptState getState() {
        return state;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setState(ResetPasswordAttemptState state) {
        this.state = state;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setSmsCodeAttemptsCount(int smsCodeAttemptsCount) {
        this.smsCodeAttemptsCount = smsCodeAttemptsCount;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    @Override
    public InternetAddress getInternetAddress() throws UnsupportedEncodingException {
        return new InternetAddress(emailAddress, userName);
    }
}
