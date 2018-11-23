package net.readybid.api.auth.registration;

import net.readybid.auth.user.UserStatusDetails;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface EmailAddressVerificationService {
    String createToken(ObjectId userId, String emailAddress);

    int getTimeToLiveInHours();

    Map<String, Object> parseToken(String token);

    boolean isEmailAddressVerificationExpired(UserStatusDetails status);
}
