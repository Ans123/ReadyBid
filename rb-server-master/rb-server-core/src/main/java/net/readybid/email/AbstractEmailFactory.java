package net.readybid.email;

import net.readybid.templates.TemplateService;
import net.readybid.user.BasicUserDetails;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by DejanK on 11/22/2016.
 *
 */
public abstract class AbstractEmailFactory {

    private final TemplateService templateService;
    protected final EmailService emailService;

    public AbstractEmailFactory(TemplateService templateService, EmailService emailService){
        this.templateService = templateService;
        this.emailService = emailService;
    }

    protected Email createEmail(WithInternetAddress receiver, String subject, Map<String, String> model) throws Exception{
        return createEmail(receiver.getInternetAddress(), subject, model);
    }

    protected Email createEmail(WithInternetAddress receiver, String subject, Map<String, String> model, String template) throws Exception{
        return createEmail(receiver.getInternetAddress(), subject, model, template, null);
    }

    protected Email createEmail(String receiverEmailAddress, String subject, Map<String, String> model) throws Exception{
        return createEmail(createReceiver(receiverEmailAddress), subject, model);
    }

    protected Email createEmail(String receiverEmailAddress, String subject, Map<String, String> model, String template) throws Exception{
        return createEmail(createReceiver(receiverEmailAddress), subject, model, template, null);
    }

    private Email createEmail(InternetAddress receiverAddress, String subject, Map<String, String> model) throws Exception{
        return createEmail(receiverAddress, subject, model, TemplateService.SIMPLE_TEMPLATE, null);
    }

    protected Email createEmail(InternetAddress receiverAddress, String subject, Map<String, String> model, InternetAddress[] ccs) throws Exception {
        return createEmail(receiverAddress, subject, model, TemplateService.SIMPLE_TEMPLATE, ccs);
    }

    private Email createEmail(InternetAddress receiverAddress, String subject, Map<String, String> model, String template, InternetAddress[] ccs) throws Exception{
        return new EmailBuilder()
                .setFrom(emailService.getOfficialSender())
                .setTo(receiverAddress)
                .setCC(ccs)
                .setSubject(subject)
                .setHtmlBody(templateService.parseTemplate(template, model))
                .build();
    }

    private InternetAddress createReceiver(String receiverEmailAddress) throws UnsupportedEncodingException {
        return new InternetAddress(receiverEmailAddress, receiverEmailAddress);
    }

    private InternetAddress createReceiver(BasicUserDetails authUser) throws UnsupportedEncodingException {
        return new InternetAddress(authUser.getEmailAddress(), authUser.getFullName());
    }

    protected String urlEncode(String token) throws UnsupportedEncodingException{
        return URLEncoder.encode(token, "UTF-8");
    }
}
