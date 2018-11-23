package net.readybid.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import net.readybid.amazon.AwsEmailSendingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static org.mockito.ArgumentMatchers.any;

/**
 * Created by DejanK on 10/6/2016.
 *
 */
public class AwsEmailSendingServiceTest {

    private AwsEmailSendingService $service;

    private AmazonSimpleEmailServiceClient client;

    @Before
    public void before() {
        client = Mockito.mock(AmazonSimpleEmailServiceClient.class);

        $service = new AwsEmailSendingService(client);
    }

    @Test
    public void testSend() throws Exception {
        final SendRawEmailResult sendRawEmailResult = Mockito.mock(SendRawEmailResult.class);
        final String SENDING_RESULT = "any string";
        Mockito.when(client.sendRawEmail(any(SendRawEmailRequest.class))).thenReturn(sendRawEmailResult);
        Mockito.when(sendRawEmailResult.getMessageId()).thenReturn(SENDING_RESULT);

        String $result = $service.send(buildEmail());

        Mockito.verify(client, Mockito.times(1)).sendRawEmail(any(SendRawEmailRequest.class));
        Assert.assertEquals(SENDING_RESULT, $result);
    }

    private Email buildEmail() throws AddressException {
        return new EmailBuilder()
                .setFrom(new InternetAddress("test@email.net"))
                .setTo(new InternetAddress("test@email.net"))
                .setSubject("subject")
                .setHtmlBody("body")
                .build();
    }
}