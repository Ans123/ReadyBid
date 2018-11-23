package net.readybid.amazon;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import net.readybid.email.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by DejanK on 10/4/2016.
 *
 */
@Service
public class AwsEmailSendingService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Autowired
    public AwsEmailSendingService(AmazonSimpleEmailService amazonSimpleEmailService){
        this.amazonSimpleEmailService = amazonSimpleEmailService;
    }

    public String send(Email email) throws IOException, MessagingException {
        final SendRawEmailResult result = amazonSimpleEmailService.sendRawEmail(createRequest(email.getMimeMessage()));
        return result.getMessageId();
    }

    private SendRawEmailRequest createRequest(MimeMessage mimeMessage) throws IOException, MessagingException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mimeMessage.writeTo(outputStream);
        final RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
        return new SendRawEmailRequest(rawMessage);
    }
}