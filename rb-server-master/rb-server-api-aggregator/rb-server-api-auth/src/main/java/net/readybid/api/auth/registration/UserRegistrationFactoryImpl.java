package net.readybid.api.auth.registration;

import net.readybid.api.auth.web.SignUpRequest;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.invitation.SignUpWithInvitationRequest;
import net.readybid.auth.password.PasswordService;
import net.readybid.auth.tutorials.Tutorial;
import net.readybid.auth.user.UserRegistration;
import net.readybid.auth.user.UserStatus;
import net.readybid.auth.user.UserStatusDetails;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.core.UserAccountFactory;
import net.readybid.user.BasicUserDetails;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class UserRegistrationFactoryImpl implements UserRegistrationFactory {

    private final PasswordService passwordService;
    private final UserAccountFactory userAccountFactory;

    @Autowired
    public UserRegistrationFactoryImpl(
            PasswordService passwordService,
            UserAccountFactory userAccountFactory
    ) {
        this.passwordService = passwordService;
        this.userAccountFactory = userAccountFactory;
    }

    @Override
    public UserRegistration create(SignUpRequest signUpRequest) {
        return createUserRegistration(new ObjectId(), signUpRequest, new CreationDetails((BasicUserDetails) null));
    }

    @Override
    public UserRegistration recreate(ObjectId userRegistrationId, SignUpRequest signUpRequest, CreationDetails created) {
        return createUserRegistration(userRegistrationId, signUpRequest, created);
    }

    @Override
    public UserStatusDetails createActiveUserStatus() {
        return new UserStatusDetails(UserStatus.ACTIVE);
    }

    @Override
    public UserRegistration createFromInvitation(SignUpWithInvitationRequest signUpRequest, Account account) {
        final UserRegistrationImpl userRegistration = new UserRegistrationImpl();

        userRegistration.setId(new ObjectId());
        userRegistration.setFirstName(signUpRequest.firstName);
        userRegistration.setLastName(signUpRequest.lastName);
        userRegistration.setEmailAddress(signUpRequest.emailAddress.toLowerCase());

        userRegistration.setPassword(passwordService.encode(signUpRequest.password));
        userRegistration.setPhone(signUpRequest.phone);

        userRegistration.setTutorials(Tutorial.asList());

        final UserAccount userAccount = userAccountFactory.createUserAccountFromInvitation(userRegistration, account, signUpRequest.jobTitle);
        userRegistration.addFirstUserAccount(userAccount);

        userRegistration.setCreated(new CreationDetails());
        userRegistration.setStatus(new UserStatusDetails(userRegistration.getCreated(), UserStatus.ACTIVE));
        userRegistration.markLastChange();

        return userRegistration;
    }

    private UserRegistration createUserRegistration(ObjectId userId, SignUpRequest signUpRequest, CreationDetails created) {
        final UserRegistrationImpl userRegistration = new UserRegistrationImpl();

        userRegistration.setId(userId);
        userRegistration.setFirstName(signUpRequest.firstName);
        userRegistration.setLastName(signUpRequest.lastName);
        userRegistration.setEmailAddress(signUpRequest.emailAddress.toLowerCase());

        userRegistration.setPassword(passwordService.encode(signUpRequest.password));
        userRegistration.setPhone(signUpRequest.phone);

        userRegistration.setTutorials(Tutorial.asList());
        userRegistration.setCreated(created);
        userRegistration.setStatus(new UserStatusDetails(userRegistration.getCreated(), UserStatus.PENDING_EMAIL_VERIFICATION));
        userRegistration.markLastChange();

        return userRegistration;
    }
}
