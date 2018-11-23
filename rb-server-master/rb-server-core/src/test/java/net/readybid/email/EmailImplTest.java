package net.readybid.email;

import org.junit.Test;

import javax.mail.internet.InternetAddress;

/**
 * Created by DejanK on 10/5/2016.
 *
 */
public class EmailImplTest {

    public static final String SENDER_EMAIL_ADDRESS = "any@email.address";
    public static final String SENDER_NAME = "any name";
    public static final String RECEIVER_EMAIL_ADDRESS = "any@email.address1";
    public static final String RECEIVER_NAME = "any name 1";
    public static final String SUBJECT = "subject";
    public static final String HTML_BODY = "htmlBody";

    @Test
    public void testGetMimeMessage() throws Exception {
        Email email = new EmailBuilder()
                .setFrom(new InternetAddress(SENDER_EMAIL_ADDRESS, SENDER_NAME))
                .setTo(new InternetAddress(RECEIVER_EMAIL_ADDRESS, RECEIVER_NAME))
                .setSubject(SUBJECT)
                .setHtmlBody(HTML_BODY)
                .build();

        EmailImplAssert.that(email)
                .hasSenderAddress(SENDER_EMAIL_ADDRESS)
                .hasSenderName(SENDER_NAME)
                .hasReceiverAddress(RECEIVER_EMAIL_ADDRESS)
                .hasReceiverName(RECEIVER_NAME)
                .hasSubject(SUBJECT)
                .hasHtmlBody(HTML_BODY);
    }
}