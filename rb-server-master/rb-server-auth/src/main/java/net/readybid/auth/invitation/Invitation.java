package net.readybid.auth.invitation;

import net.readybid.user.BasicUserDetails;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

/**
 * Created by DejanK on 5/18/2017.
 *
 */
public interface Invitation extends BasicUserDetails {
    String getToken();

    ObjectId getTargetId();

    Date getExpiryDate();

    ObjectId getAccountId();

    InvitationStatusDetails getStatus();

    String getJobTitle();

    String getAccountName();
}
