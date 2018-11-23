package net.readybid.api.auth.resetpassword;

import net.readybid.api.auth.web.ResetPasswordRequest;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.auth.password.PasswordService;
import net.readybid.auth.user.UserRegistration;
import net.readybid.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final ResetPasswordAttemptFactory resetPasswordAttemptFactory;
    private final ResetPasswordEmailService resetPasswordEmailService;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final ResetPasswordRepository resetPasswordRepository;
    private final PasswordService passwordService;
    private final ResetPasswordSmsService smsService;

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SMS_CODE_LENGTH = 6;
    private static final SecureRandom rnd = new SecureRandom();
    private static final long VERIFY_EMAIL_LINK_TTL = 86400000L; // 24 hours
    private static final int VERIFY_EMAIL_LINK_TTL_IN_HOURS = 24; // 24 hours
    private static final long SMS_CODE_TTL = 3600000L; // 1 hour
    private static final long PASSWORD_TTL = 1800000L; // 1 hour
    private static final int MAX_SMS_CODE_ATTEMPTS = 5; // 1 hour


    @Autowired
    public ResetPasswordServiceImpl(
            ResetPasswordAttemptFactory resetPasswordAttemptFactory,
            ResetPasswordEmailService resetPasswordEmailService,
            ResetPasswordTokenService resetPasswordTokenService,
            ResetPasswordSmsService resetPasswordSmsService,
            ResetPasswordRepository resetPasswordRepository,
            PasswordService passwordService
    ) {
        this.resetPasswordAttemptFactory = resetPasswordAttemptFactory;
        this.resetPasswordEmailService = resetPasswordEmailService;
        this.resetPasswordTokenService = resetPasswordTokenService;
        this.resetPasswordRepository = resetPasswordRepository;
        this.passwordService = passwordService;
        this.smsService = resetPasswordSmsService;
    }

    @Override
    public Map<String, Object> resetPasswordAttempt(ResetPasswordRequest resetPasswordRequest) {
        if (resetPasswordRequest.token == null) {
            return newResetPasswordAttempt(resetPasswordRequest);
        } else {
            final Map<String, Object> tokenPayload = resetPasswordTokenService.parseToken(resetPasswordRequest.token);
            final ResetPasswordAttemptState tokenState = getTokenState(tokenPayload);
            final String resetPasswordAttemptId = String.valueOf(tokenPayload.get(ResetPasswordTokenService.ID_FIELD));
            final ResetPasswordAttempt attempt = resetPasswordRepository.findById(resetPasswordAttemptId);

            if (attempt == null) {
                throw new NotFoundException();
            } else if (attempt.hasExpired()) {
                return removeAttempt(attempt);
            } else if (attempt.shouldVerifyEmailAddress()) {
                return verifyEmailAddress(attempt, tokenState);
            } else if (attempt.shouldVerifyMobilePhone()) {
                return verifyMobilePhone(attempt, tokenState, resetPasswordRequest.code);
            } else if (attempt.shouldSetPassword()) {
                return setPassword(attempt, tokenState, resetPasswordRequest.password);
            } else {
                return removeAttempt(attempt);
            }
        }
    }

    private ResetPasswordAttemptState getTokenState(Map<String, Object> tokenPayload) {
        try {
            return ResetPasswordAttemptState.valueOf(String.valueOf(tokenPayload.get(ResetPasswordTokenService.STATE_FIELD)));
        } catch (IllegalArgumentException e){
            throw new NotFoundException();
        }
    }

    private Map<String, Object> setPassword(ResetPasswordAttempt attempt, ResetPasswordAttemptState tokenState, String password) {
        if(!tokenState.equals(ResetPasswordAttemptState.SET_PASSWORD)) {throw new NotFoundException();}
        if(password == null || password.isEmpty()) {
            throw new BadRequestException();
        } else {
            resetPasswordRepository.updatePassword(attempt.getUserId(), passwordService.encode(password));
            resetPasswordRepository.delete(attempt);

            final Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            return response;
        }
    }

    private Map<String, Object> removeAttempt(ResetPasswordAttempt attempt) {
        resetPasswordRepository.delete(attempt);
        throw new NotFoundException();
    }

    private Map<String, Object> verifyMobilePhone(ResetPasswordAttempt attempt, ResetPasswordAttemptState tokenState, String code) {
        if(!tokenState.equals(ResetPasswordAttemptState.VERIFY_MOBILE_PHONE)) {throw new NotFoundException();}
        if(code == null || code.isEmpty()){
            throw new BadRequestException();
        } else if(MAX_SMS_CODE_ATTEMPTS <= attempt.getSmsCodeAttemptsCount()) {
            return removeAttempt(attempt);
        } else if (passwordService.equals(code.toUpperCase(), attempt.getSmsCode())){
            return createSetPasswordResponse(attempt);
        } else {
            return createSmsCodeVerificationFailedResponse(attempt);
        }
    }

    private Map<String, Object> createSmsCodeVerificationFailedResponse(ResetPasswordAttempt attempt) {
        int attemptsCount = resetPasswordRepository.increaseSmsCodeAttemptsCount(attempt);

        final Map<String, Object> response = new HashMap<>();
        response.put("attemptsLeft", getAttemptsLeft(attemptsCount));
        response.put("message", "INVALID_CODE");
        return response;
    }

    private Map<String, Object> createSetPasswordResponse(ResetPasswordAttempt attempt) {
        final long expiresAt = new Date().getTime() + PASSWORD_TTL;

        final ResetPasswordAttempt setPasswordAttempt = resetPasswordAttemptFactory.createSetPasswordAttempt(attempt, expiresAt);
        resetPasswordRepository.update(setPasswordAttempt);

        final Map<String, Object> response = new HashMap<>();
        response.put("token", resetPasswordTokenService.createToken(setPasswordAttempt, expiresAt));
        response.put("message", "OK");
        return response;
    }

    private Map<String, Object> verifyEmailAddress(ResetPasswordAttempt attempt, ResetPasswordAttemptState tokenState) {
        if(!tokenState.equals(ResetPasswordAttemptState.VERIFY_EMAIL_ADDRESS)) {throw new NotFoundException();}
        return createSetPasswordResponse(attempt);
        /**
         * 2017-08-17
         * Phone number check is disabled as a request from Joseph.
         *
         * To enable it again, uncomment following line and set up Twillio number and API key in SmsService
         */
        // return createCheckPhoneNumberResponse(attempt);
    }

    private Map<String, Object> createCheckPhoneNumberResponse(ResetPasswordAttempt attempt) {
        final long expiresAt = new Date().getTime() + SMS_CODE_TTL;
        final String smsCode = createCode(SMS_CODE_LENGTH);

        final ResetPasswordAttempt phoneAttempt = resetPasswordAttemptFactory.createVerifyMobilePhoneAttempt(attempt, passwordService.encode(smsCode), expiresAt);
        resetPasswordRepository.update(phoneAttempt);
        smsService.sendSms(phoneAttempt.getPhoneNumber(), smsCode);

        final Map<String, Object> response = new HashMap<>();
        response.put("attemptsLeft", getAttemptsLeft(phoneAttempt.getSmsCodeAttemptsCount()));
        response.put("token", resetPasswordTokenService.createToken(phoneAttempt, expiresAt));
        return response;
    }

    private Map<String, Object> newResetPasswordAttempt(ResetPasswordRequest resetPasswordRequest) {
        if(resetPasswordRequest.emailAddress == null || resetPasswordRequest.emailAddress.isEmpty()){
            throw new BadRequestException();
        }

        final UserRegistration userRegistration = resetPasswordRepository.findUserRegistration(resetPasswordRequest.emailAddress.toLowerCase());
        if(userRegistration == null || userRegistration.isPendingEmailAddressVerification()){
            resetPasswordEmailService.sendUserDoesNotExistEmail(resetPasswordRequest.emailAddress);
        } else {
            final long expiresAt = new Date().getTime() + VERIFY_EMAIL_LINK_TTL;
            final ResetPasswordAttempt resetPasswordAttempt = resetPasswordAttemptFactory.createVerifyEmailAddressAttempt(userRegistration, expiresAt);
            resetPasswordRepository.create(resetPasswordAttempt);
            final String resetPasswordToken = resetPasswordTokenService.createToken(resetPasswordAttempt, expiresAt);
            resetPasswordEmailService.sendResetPasswordInstructionsEmail(resetPasswordAttempt, resetPasswordToken, VERIFY_EMAIL_LINK_TTL_IN_HOURS);
        }
        return null;
    }

    private String createCode(int length) {
        StringBuilder sb = new StringBuilder( length );
        for( int i = 0; i < length; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    private int getAttemptsLeft(int attemptsCount) {
        return MAX_SMS_CODE_ATTEMPTS - attemptsCount + 1;
    }
}
