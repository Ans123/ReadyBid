package net.readybid.api.auth.registration;

import net.readybid.api.auth.web.SignUpRequest;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.invitation.SignUpWithInvitationRequest;
import net.readybid.auth.user.UserRegistration;
import net.readybid.auth.user.UserStatusDetails;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public interface UserRegistrationFactory {
    UserRegistration create(SignUpRequest signUpRequest);

    UserRegistration recreate(ObjectId userRegistrationId, SignUpRequest signUpRequest, CreationDetails created);

    UserStatusDetails createActiveUserStatus();

    UserRegistration createFromInvitation(SignUpWithInvitationRequest signUpRequest, Account accoun);
}
