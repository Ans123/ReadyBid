package net.readybid.api.auth.registration;

import net.readybid.api.auth.web.SignUpRequest;
import net.readybid.auth.user.UserRegistration;
import net.readybid.email.Email;
import net.readybid.email.EmailService;
import net.readybid.mongodb.RbDuplicateKeyException;
import net.readybid.utils.CreationDetails;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class MockUserRegistrationServiceImpl implements MockUserRegistrationService {

    private final MockUserRegistrationRepository mockedUserRepository;
    private final UserRegistrationFactory userRegistrationFactory;
    private final UserRegistrationEmailFactory userRegistrationEmailFactory;
    private final EmailService emailService;


    @Autowired
    public MockUserRegistrationServiceImpl(
            MockUserRegistrationRepository mockedUserRepository,
            UserRegistrationFactory userRegistrationFactory,
            UserRegistrationEmailFactory userRegistrationEmailFactory,
            EmailService emailService
    ) {
        this.mockedUserRepository = mockedUserRepository;
        this.userRegistrationFactory = userRegistrationFactory;
        this.userRegistrationEmailFactory = userRegistrationEmailFactory;
        this.emailService = emailService;
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        final UserRegistration userRegistration = mockedUserRepository.findUserRegistration(signUpRequest.emailAddress);

        if(userRegistration == null){
            createUser(signUpRequest);
        } else {
            recreateUser(userRegistration.getId(), signUpRequest, userRegistration.getCreated());
        }
    }

    private void recreateUser(ObjectId userRegistrationId, SignUpRequest signUpRequest, CreationDetails created) {
        final UserRegistration userRegistration = userRegistrationFactory.recreate(userRegistrationId, signUpRequest, created);
        mockedUserRepository.recreateUserRegistration(userRegistration);
        notifyJoeOfNewSignUp(userRegistration);
    }

    private void createUser(SignUpRequest signUpRequest) {
        try {
            final UserRegistration userRegistration = userRegistrationFactory.create(signUpRequest);
            mockedUserRepository.createUserRegistration(userRegistration);
            notifyJoeOfNewSignUp(userRegistration);
        } catch (RbDuplicateKeyException emailAddressAlreadyExists){
            signUp(signUpRequest);
        }
    }

    private void notifyJoeOfNewSignUp(UserRegistration userRegistration){
        final Email email = userRegistrationEmailFactory.createNotifyJoeOfNewSignUpEmail(userRegistration);
        emailService.send(email);
    }
}
