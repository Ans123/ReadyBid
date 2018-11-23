package net.readybid.api.auth.resetpassword;

import net.readybid.auth.user.UserRegistration;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface ResetPasswordRepository {
    void create(ResetPasswordAttempt resetPasswordAttempt);

    ResetPasswordAttempt findById(String resetPasswordAttemptId);

    void update(ResetPasswordAttempt attempt);

    UserRegistration findUserRegistration(String emailAddress);

    void updatePassword(ObjectId userId, String password);

    void delete(ResetPasswordAttempt attempt);

    int increaseSmsCodeAttemptsCount(ResetPasswordAttempt attempt);
}
