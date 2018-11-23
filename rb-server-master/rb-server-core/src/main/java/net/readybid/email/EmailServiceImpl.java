package net.readybid.email;

import net.readybid.amazon.AwsEmailSendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by DejanK on 10/4/2016.
 *
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final AwsEmailSendingService awsEmailSendingService;

    private final String clientAddress;
    private final String clientAddressRelative;
    private final InternetAddress officialSender;

    @Autowired
    public EmailServiceImpl(Environment environment, AwsEmailSendingService awsEmailSendingService) throws UnsupportedEncodingException {
        this.awsEmailSendingService = awsEmailSendingService;
        this.clientAddress = environment.getProperty("client.address");
        this.clientAddressRelative = environment.getProperty("client.address.relative");
        officialSender = new InternetAddress(environment.getProperty("email.sender.email"), environment.getProperty("email.sender.name"));
    }

    @Override
    public Map<String, String> prepareModel() {
        final Map<String, String> model = new HashMap<>();
        model.put("CLIENT_ADDRESS", clientAddress);
        model.put("CLIENT_APP_ADDRESS", clientAddressRelative);
        model.put("MESSAGE_2", "");
        model.put("HEADER_TEXT", "");
        return model;
    }

    @Override
    public InternetAddress getOfficialSender() {
        return officialSender;
    }

    @Override
    public void send(Email email){
        CompletableFuture.supplyAsync(() -> sendEmail(email))
                .exceptionally(this::logSendingFailure);
    }

    private Object logSendingFailure(Throwable throwable) {
        // todo
        throwable.printStackTrace();
        return null;
    }

    private Object sendEmail(Email email) {
        try {
            return awsEmailSendingService.send(email);
        } catch (MessagingException | IOException e) {
            throw new EmailSendingFailedException(e);
        }
    }
}
