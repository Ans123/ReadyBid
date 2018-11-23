package net.readybid.email;

import com.nitorcreations.junit.runners.NestedRunner;
import net.readybid.amazon.AwsEmailSendingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.Map;

/**
 * Created by DejanK on 10/6/2016.
 *
 */
@RunWith(NestedRunner.class)
public class EmailServiceImplTest {

    private EmailServiceImpl $service;

    private AwsEmailSendingService awsEmailSendingService;

    private final String CLIENT_ADDRESS = "any url";
    private final String CLIENT_ADDRESS_RELATIVE = "any angular url";
    private final String OFFICIAL_EMAIL_ADDRESS = "any email address";
    private final String OFFICIAL_NAME = "any name";

    @Before
    public void before() throws Exception{
        final Environment environment = Mockito.mock(Environment.class);
        awsEmailSendingService = Mockito.mock(AwsEmailSendingService.class);

        Mockito.when(environment.getProperty("client.address")).thenReturn(CLIENT_ADDRESS);
        Mockito.when(environment.getProperty("client.address.relative")).thenReturn(CLIENT_ADDRESS_RELATIVE);
        Mockito.when(environment.getProperty("email.sender.email")).thenReturn(OFFICIAL_EMAIL_ADDRESS);
        Mockito.when(environment.getProperty("email.sender.name")).thenReturn(OFFICIAL_NAME);

        $service = new EmailServiceImpl(environment, awsEmailSendingService);
    }

    public class PrepareModel{

        private Map<String, String> $result;

        @Before
        public void before() {
            $result = $service.prepareModel();
        }

        @Test
        public void shouldReturnMapWithClientAddress() throws Exception {
            Assert.assertEquals(CLIENT_ADDRESS, $result.get("CLIENT_ADDRESS"));
        }

        @Test
        public void shouldReturnMapWithClientAppAddress() throws Exception {
            Assert.assertEquals(CLIENT_ADDRESS_RELATIVE, $result.get("CLIENT_APP_ADDRESS"));
        }
    }

    public class GetOfficialSender{
        private InternetAddress $senderAddress;

        @Before
        public void before() {
            $senderAddress = $service.getOfficialSender();
        }

        @Test
        public void shouldReturnInternetAddressWithOfficialEmailAddress() throws Exception {
            Assert.assertEquals(OFFICIAL_EMAIL_ADDRESS, $senderAddress.getAddress());
        }

        @Test
        public void shouldReturnInternetAddressWithOfficialName() throws Exception {
            Assert.assertEquals(OFFICIAL_NAME, $senderAddress.getPersonal());
        }
    }

    public class Send {

        private Email email;

        @Before
        public void before() {
            email = Mockito.mock(Email.class);
        }

        @Test
        public void shouldDelegateSendingToAwsEmailSendingService() throws Exception {
            final String SENDING_RESULT_ID = "someId from aws";
            Mockito.when(awsEmailSendingService.send(email)).thenReturn(SENDING_RESULT_ID);

//            $service.send(email);

//            Mockito.verify(awsEmailSendingService, Mockito.times(1)).send(email);
//            Assert.assertEquals(SENDING_RESULT_ID, $result);
        }

//        @Test(expected = EmailSendingFailedException.class)
        public void shouldThrowEmailSendingFailedExceptionIfAwsSendingThrowsMessagingException() throws Exception {
            Mockito.doThrow(new MessagingException()).when(awsEmailSendingService).send(email);

//            $service.send(email);
        }

//        @Test(expected = EmailSendingFailedException.class)
        public void shouldThrowEmailSendingFailedExceptionIfAwsSendingThrowsIOException() throws Exception {
            Mockito.doThrow(new IOException()).when(awsEmailSendingService).send(email);

//            $service.send(email);
        }
    }
}