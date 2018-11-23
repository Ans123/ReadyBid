package net.readybid.api.auth.registration;

import net.readybid.api.auth.web.SignUpRequest;
import net.readybid.api.auth.web.VerifyEmailAddressRequest;
import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.auth.account.core.Account;
import net.readybid.auth.account.core.AccountService;
import net.readybid.auth.invitation.Invitation;
import net.readybid.auth.invitation.SignUpWithInvitationRequest;
import net.readybid.auth.user.AddAccountWithInvitationRequest;
import net.readybid.auth.user.UserRegistration;
import net.readybid.auth.useraccount.UserAccount;
import net.readybid.auth.useraccount.core.UserAccountCreationService;
import net.readybid.auth.useraccount.core.UserAccountFactory;
import net.readybid.email.Email;
import net.readybid.email.EmailService;
import net.readybid.exceptions.NotAllowedException;
import net.readybid.mongodb.RbDuplicateKeyException;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRegistrationRepository userRepository;
    private final UserRegistrationFactory userRegistrationFactory;
    private final UserRegistrationEmailFactory userRegistrationEmailFactory;
    private final EmailAddressVerificationService emailAddressVerificationService;
    private final EmailService emailService;
    private final AccountService accountService;
    private final UserAccountFactory userAccountFactory;
    private final UserAccountCreationService userAccountCreationService;

    @Autowired
    public UserRegistrationServiceImpl(
            UserRegistrationRepository userRepository,
            UserRegistrationFactory userRegistrationFactory,
            UserRegistrationEmailFactory userRegistrationEmailFactory,
            EmailAddressVerificationService emailAddressVerificationService,
            EmailService emailService,
            AccountService accountService,
            UserAccountFactory userAccountFactory,
            UserAccountCreationService userAccountCreationService
    ) {
        this.userRepository = userRepository;
        this.userRegistrationFactory = userRegistrationFactory;
        this.userRegistrationEmailFactory = userRegistrationEmailFactory;
        this.emailAddressVerificationService = emailAddressVerificationService;
        this.emailService = emailService;
        this.accountService = accountService;
        this.userAccountFactory = userAccountFactory;
        this.userAccountCreationService = userAccountCreationService;
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        final UserRegistration userRegistration = userRepository.findUserRegistration(signUpRequest.emailAddress.toLowerCase());

        if(userRegistration == null){
            createUser(signUpRequest);
        } else if(userRegistration.isPendingEmailAddressVerification()){
            if(emailAddressVerificationService.isEmailAddressVerificationExpired(userRegistration.getStatus())){
                recreateUser(userRegistration.getId(), signUpRequest, userRegistration.getCreated());
            } else {
                handleEmailAddressVerificationAlreadyInProgress(userRegistration);
            }
        } else {
            handleAlreadyRegisteredUser(userRegistration);
        }
    }

    @Override
    public void verifyEmailAddress(VerifyEmailAddressRequest verifyEmailAddressRequest) {
        final Map<String, Object> tokenPayload = emailAddressVerificationService.parseToken(verifyEmailAddressRequest.token);
        final String emailAddress = tokenPayload.get("email").toString();
        final UserRegistration userRegistration = userRepository.findUserRegistration(emailAddress);
        if(userRegistration != null && userRegistration.getId().toString().equals(tokenPayload.get("id").toString())){
            userRegistration.setStatus(userRegistrationFactory.createActiveUserStatus());
            userRepository.updateUserStatus(userRegistration);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public UserRegistration signUpWithInvitation(SignUpWithInvitationRequest signUpWithInvitationRequest, Invitation invitation) {
        if(invitation == null || !signUpWithInvitationRequest.emailAddress.equalsIgnoreCase(String.valueOf(invitation.getEmailAddress())))
            throw new NotAllowedException();

        final Account account = accountService.getAccount(invitation.getAccountId());
        final UserRegistration userRegistration = userRegistrationFactory.createFromInvitation(signUpWithInvitationRequest, account);

        userRepository.createUserAndUserAccount(userRegistration);
        userAccountCreationService.onNewUserAccount(userRegistration.getCurrentUserAccount());
        return userRegistration;
    }

    @Override
    public UserRegistration addAccountWithInvitation(Invitation invitation, AddAccountWithInvitationRequest addAccountWithInvitationRequest) {
        if(!invitation.getEmailAddress().equalsIgnoreCase(addAccountWithInvitationRequest.emailAddress))
            throw new NotAllowedException();

        final UserRegistration userRegistration = userRepository.findUserRegistration(invitation.getEmailAddress());
        final Account account = accountService.getAccount(invitation.getAccountId());
        final UserAccount userAccount = userAccountFactory.createUserAccount(account, userRegistration, addAccountWithInvitationRequest.jobTitle);

        userRegistration.addUserAccount(userAccount);
        userRepository.addUserAccount(userAccount, userRegistration.getLastChangeTimestamp());
        userAccountCreationService.onNewUserAccount(userAccount);
        return userRegistration;
    }

    @Override
    public UserRegistration findUserRegistration(String emailAddress) {
        return userRepository.findUserRegistration(emailAddress);
    }

    private void handleAlreadyRegisteredUser(UserRegistration userRegistration) {
        emailService.send(userRegistrationEmailFactory.createUserAlreadyRegisteredEmail(userRegistration));
    }

    private void handleEmailAddressVerificationAlreadyInProgress(UserRegistration userRegistration) {
        emailService.send(userRegistrationEmailFactory.createEmailAddressVerificationInProgressEmail(userRegistration));
    }

    private void recreateUser(ObjectId userRegistrationId, SignUpRequest signUpRequest, CreationDetails created) {
        final UserRegistration userRegistration = userRegistrationFactory.recreate(userRegistrationId, signUpRequest, created);
        userRepository.recreateUserRegistration(userRegistration);
        sendVerifyEmailAddressEmail(userRegistration);
    }

    private void createUser(SignUpRequest signUpRequest) {
        try {
            final UserRegistration userRegistration = userRegistrationFactory.create(signUpRequest);
            userRepository.createUserRegistration(userRegistration);
            sendVerifyEmailAddressEmail(userRegistration);
        } catch (RbDuplicateKeyException emailAddressAlreadyExists){
            signUp(signUpRequest);
        }
    }

    private void sendVerifyEmailAddressEmail(UserRegistration userRegistration){
        final String token = emailAddressVerificationService.createToken(userRegistration.getId(), userRegistration.getEmailAddress());
        final Email email = userRegistrationEmailFactory.createVerifyEmailAddressEmail(userRegistration, token, emailAddressVerificationService.getTimeToLiveInHours());
        emailService.send(email);
    }
}
