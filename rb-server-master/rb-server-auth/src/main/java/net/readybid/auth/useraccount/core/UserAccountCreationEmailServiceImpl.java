package net.readybid.auth.useraccount.core;

import net.readybid.auth.useraccount.UserAccount;
import net.readybid.email.AbstractEmailFactory;
import net.readybid.email.Email;
import net.readybid.email.EmailCreationException;
import net.readybid.email.EmailService;
import net.readybid.templates.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class UserAccountCreationEmailServiceImpl extends AbstractEmailFactory implements UserAccountCreationEmailService {

    private final String contactUsEmailAddress;

    @Autowired
    public UserAccountCreationEmailServiceImpl(TemplateService templateService, EmailService emailService, Environment env) {
        super(templateService, emailService);
        contactUsEmailAddress = env.getRequiredProperty("email.receiver.contactUs");
    }

    @Override
    public void notifyAbout(UserAccount userAccount) {
        try {
            final Map<String, String> model = emailService.prepareModel();
            createNotificationEmailModel(model, userAccount);
            final Email email = createEmail(contactUsEmailAddress, "New User Account", model);
            emailService.send(email);
        } catch (Exception e){
            throw new EmailCreationException(e);
        }
    }

    private void createNotificationEmailModel(Map<String, String> model, UserAccount userAccount) {
        model.put("FIRST_NAME", "Joseph");
        model.put("MESSAGE",
                String.format("New User Account on LIVE site: <br /><br />" +
                                "Account Type: %s <br />" +
                                "Account Name: %s <br />" +
                                "Job Title: %s <br />" +
                                "Name: %s <br />" +
                                "Email: %s<br />" +
                                "Phone: %s<br /> " +
                                "First Name: %s <br />" +
                                "Last Name: %s <br />",
                        userAccount.getAccountType(),
                        userAccount.getAccountName(),
                        userAccount.getJobTitle(),
                        userAccount.getFullName(),
                        userAccount.getEmailAddress(),
                        userAccount.getPhone(),
                        userAccount.getFirstName(),
                        userAccount.getLastName()
                )
        );
        model.put("LINK_URL", "");
        model.put("LINK_TEXT", "");
    }
}
