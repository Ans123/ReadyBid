package net.readybid.api.auth.registration;

import net.readybid.auth.user.UserRegistration;
import net.readybid.email.AbstractEmailFactory;
import net.readybid.email.Email;
import net.readybid.email.EmailCreationException;
import net.readybid.email.EmailService;
import net.readybid.templates.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class UserRegistrationEmailFactoryImpl extends AbstractEmailFactory implements UserRegistrationEmailFactory {

    private final String contactUsEmailAddress;

    @Autowired
    public UserRegistrationEmailFactoryImpl(TemplateService templateService, EmailService emailService, Environment env) {
        super(templateService, emailService);
        contactUsEmailAddress = env.getRequiredProperty("email.receiver.contactUs");
    }

    @Override
    public Email createVerifyEmailAddressEmail(UserRegistration userRegistration, String token, int timeToLiveInHours) {
        try {
            final Map<String, String> model = emailService.prepareModel();
            createEmailAddressVerificationEmailModel(model, userRegistration, token, timeToLiveInHours);
            return createEmail(userRegistration, "Email address verification", model);
        } catch (Exception e){
            throw new EmailCreationException(e);
        }

    }

    @Override
    public Email createEmailAddressVerificationInProgressEmail(UserRegistration userRegistration) {
        try {
            final Map<String, String> model = emailService.prepareModel();
            createEmailAddressVerificationInProgressEmailModel(model, userRegistration);
            return createEmail(userRegistration, "New registration attempt - Your account verification is already in progress", model);
        } catch (Exception e){
            throw new EmailCreationException(e);
        }
    }

    @Override
    public Email createUserAlreadyRegisteredEmail(UserRegistration userRegistration) {
        try {
            final Map<String, String> model = emailService.prepareModel();
            createUserAlreadyRegisteredEmailModel(model, userRegistration);
            return createEmail(userRegistration, "New registration attempt - Your account already exists", model);
        } catch (Exception e){
            throw new EmailCreationException(e);
        }
    }

    @Override
    public Email createNotifyJoeOfNewSignUpEmail(UserRegistration userRegistration) {
        try {
            final Map<String, String> model = emailService.prepareModel();
            model.put("FIRST_NAME", "Joseph");
            model.put("MESSAGE", String.format("New User Sign Up on LIVE site: <br /><br />Name: %s <br />Email: %s<br />Phone: %s<br /> ", userRegistration.getFullName(), userRegistration.getEmailAddress(), userRegistration.getPhone()));
            model.put("LINK_URL", "");
            model.put("LINK_TEXT", "");
            return createEmail(contactUsEmailAddress, "New User Sign Up on LIVE site", model);
        } catch (Exception e){
            throw new EmailCreationException(e);
        }
    }

    private void createEmailAddressVerificationEmailModel(Map<String, String> model, UserRegistration user, String token, int timeToLiveInHours)
            throws UnsupportedEncodingException {
        model.put("FIRST_NAME", user.getFirstName());
        model.put("MESSAGE", String.format("Thank You for registering with ReadyBID. To confirm this email address and " +
                        "activate your account please click on the link below.<br>Link is valid for the next %s hours.",
                timeToLiveInHours));
        model.put("LINK_URL", String.format("/verify-email-address?token=%s", urlEncode(token)));
        model.put("LINK_TEXT", "CLICK HERE TO ACTIVATE YOUR ACCOUNT");
    }

    private void createEmailAddressVerificationInProgressEmailModel(Map<String, String> model, UserRegistration user) {
        model.put("FIRST_NAME", user.getFirstName());
        model.put("MESSAGE", "Your email address verification is already in progress. Please try to find an email you " +
                "should have received from us with subject \"Email address verification\" in your inbox, trash, junk or " +
                "spam folders.<br>If you cannot find activation email, please wait 48 hours to pass and then try to sign up" +
                " again or send our Customers Service a message using the link below.");
        model.put("LINK_URL", "/contact-us?subject=account+activation");
        model.put("LINK_TEXT", "CLICK HERE TO CONTACT CUSTOMER SUPPORT");
    }

    private void createUserAlreadyRegisteredEmailModel(Map<String, String> model, UserRegistration user) {
        model.put("FIRST_NAME", user.getFirstName());
        model.put("MESSAGE", "Someone has tried to create new user account using this email address but account already " +
                "exists! If it was you please proceed to sign in by clicking on the link below. If you have forgot your " +
                "password, you will be able to reset it from the sign in page.");
        model.put("LINK_URL", "/sign-in");
        model.put("LINK_TEXT", "CLICK HERE TO SIGN IN");
    }
}
