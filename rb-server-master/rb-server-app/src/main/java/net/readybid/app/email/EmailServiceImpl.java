package net.readybid.app.email;

import net.readybid.amazon.AwsEmailSendingService;
import net.readybid.app.entities.email.EmailTemplate;
import net.readybid.app.interactors.email.EmailService;
import net.readybid.email.Email;
import net.readybid.email.EmailBuilder;
import net.readybid.email.EmailCreationException;
import net.readybid.templates.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service("NewEmailService")
public class EmailServiceImpl implements EmailService {

    private final AwsEmailSendingService awsEmailSendingService;

    private final String clientAddress;
    private final String clientAddressRelative;
    private final InternetAddress officialSender;

    private final TemplateService templateService;

    @Autowired
    public EmailServiceImpl(
            AwsEmailSendingService awsEmailSendingService,
            Environment environment,
            TemplateService templateService
    ) throws UnsupportedEncodingException {
        this.awsEmailSendingService = awsEmailSendingService;
        this.clientAddress = environment.getRequiredProperty("client.address");
        this.clientAddressRelative = environment.getRequiredProperty("client.address.relative");
        officialSender = new InternetAddress(
                environment.getRequiredProperty("email.sender.email"),
                environment.getRequiredProperty("email.sender.name")
        );
        this.templateService = templateService;
    }

    @Override
    public void send(EmailTemplate emailTemplate){
        send(createEmail(emailTemplate));
    }

    @Override
    public void send(List<EmailTemplate> emailTemplates) {
        emailTemplates.forEach(this::send);
    }

    private Email createEmail(EmailTemplate emailTemplate){
        try {

            final Map<String, String> model = updateModel(emailTemplate.getModel());
            return new EmailBuilder()
                    .setFrom(officialSender)
                    .setTo(emailTemplate.getReceiver())
                    .setCC(emailTemplate.getCC())
                    .setSubject(emailTemplate.getSubject())
                    .setHtmlBody(templateService.parseTemplate(emailTemplate.getHtmlTemplateName(), model))
                    .build();
        } catch (Exception e){
            throw new EmailCreationException(e);
        }
    }

    private Map<String, String> updateModel(Map<String, String> model) {
        final Map<String, String> updatedModel = new HashMap<>(model);
        updatedModel.putIfAbsent("CLIENT_ADDRESS", clientAddress);
        updatedModel.putIfAbsent("CLIENT_APP_ADDRESS", clientAddressRelative);
        updatedModel.putIfAbsent("MESSAGE_2", "");
        updatedModel.putIfAbsent("HEADER_TEXT", "");

        return updatedModel;
    }

    private void send(Email email) {
        CompletableFuture.supplyAsync(() -> sendEmail(email))
                .exceptionally(this::logSendingFailure);
    }

    private Object sendEmail(Email email) {
        try {
            return awsEmailSendingService.send(email);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object logSendingFailure(Throwable throwable) {
        // todo
        throwable.printStackTrace();
        return null;
    }
}
