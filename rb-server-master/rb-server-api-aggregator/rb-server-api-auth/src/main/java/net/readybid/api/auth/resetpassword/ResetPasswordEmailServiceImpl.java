package net.readybid.api.auth.resetpassword;

import net.readybid.email.AbstractEmailFactory;
import net.readybid.email.Email;
import net.readybid.email.EmailCreationException;
import net.readybid.email.EmailService;
import net.readybid.templates.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
@Service
public class ResetPasswordEmailServiceImpl extends AbstractEmailFactory implements ResetPasswordEmailService {

    @Autowired
    public ResetPasswordEmailServiceImpl(TemplateService templateService, EmailService emailService) {
        super(templateService, emailService);
    }

    @Override
    public void sendUserDoesNotExistEmail(String emailAddress) {
        try {
            final Map<String, String> model = emailService.prepareModel();
            createUserDoesNotExistEmailModel(model);
            final Email email = createEmail(emailAddress, "Reset password instructions", model);
            emailService.send(email);
        } catch (Exception e){
            throw new EmailCreationException(e);
        }
    }

    @Override
    public void sendResetPasswordInstructionsEmail(ResetPasswordAttempt resetPasswordAttempt, String token, int tokenTtlInHours) {
        try {
            final Map<String, String> model = emailService.prepareModel();
            createResetPasswordInstructionsEmailModel(model, resetPasswordAttempt.getUserName(), token, tokenTtlInHours);
            final Email email = createEmail(resetPasswordAttempt, "Reset password instructions", model);
            emailService.send(email);
        } catch (Exception e){
            throw new EmailCreationException(e);
        }
    }

    private void createUserDoesNotExistEmailModel(Map<String, String> model){
        model.put("FIRST_NAME", "");
        model.put("MESSAGE", "This email address is not registered.");
        model.put("LINK_URL", "/sign-up");
        model.put("LINK_TEXT", "CLICK HERE TO REGISTER");
    }

    private void createResetPasswordInstructionsEmailModel(Map<String, String> model, String userName, String token, int tokenTtlInHours) throws UnsupportedEncodingException {
        model.put("FIRST_NAME", userName);
        model.put("MESSAGE", String.format("We've received a request to reset your password. If you didn't make the request, just ignore this email. Otherwise, you can reset your password using the link below.<br>Link is valid for the next %s hours and can be used only once.", tokenTtlInHours));
        model.put("LINK_URL", String.format("/forgotten-password/verify-email-address?token=%s", urlEncode(token)));
        model.put("LINK_TEXT", "CLICK HERE TO RESET PASSWORD");
    }
}
