package net.readybid.email;

import org.junit.Test;

import javax.mail.internet.InternetAddress;

/**
 * Created by DejanK on 10/5/2016.
 *
 */
public class EmailBuilderTest {

    public static final String SENDER_EMAIL_ADDRESS = "any@email.address";
    public static final String SENDER_NAME = "any name";
    public static final String RECEIVER_EMAIL_ADDRESS = "any@email.address1";
    public static final String RECEIVER_NAME = "any name 1";
    public static final String SUBJECT = "subject";
    public static final String HTML_BODY = "htmlBody";
    public static final String TEXT_BODY = "textBody";
    public static final String ATTACHMENT = "attachment path";

    private Email $email;

    @Test
    public void shouldCreateEmail() throws Exception {
        $email = new EmailBuilder()
                .setFrom(new InternetAddress(SENDER_EMAIL_ADDRESS, SENDER_NAME))
                .setTo(new InternetAddress(RECEIVER_EMAIL_ADDRESS, RECEIVER_NAME))
                .setSubject(SUBJECT)
                .setHtmlBody(HTML_BODY)
//                .setTextBody(TEXT_BODY)
//                .addAttachment(ATTACHMENT)
                .build();

        EmailImplAssert.that($email)
                .hasSenderAddress(SENDER_EMAIL_ADDRESS)
                .hasSenderName(SENDER_NAME)
                .hasReceiverAddress(RECEIVER_EMAIL_ADDRESS)
                .hasReceiverName(RECEIVER_NAME)
                .hasSubject(SUBJECT)
                .hasHtmlBody(HTML_BODY);
//                .hasTextBody(TEXT_BODY)
//                .hasAttachment(ATTACHMENT);
    }

    @Test(expected = EmailCreationException.class)
    public void shouldThrowErrorIfFromIsNull() throws Exception {
        new EmailBuilder()
                .setTo(new InternetAddress(RECEIVER_EMAIL_ADDRESS, RECEIVER_NAME))
                .setSubject(SUBJECT)
                .setHtmlBody(HTML_BODY)
                .build();
    }

    @Test(expected = EmailCreationException.class)
    public void shouldThrowErrorIfToIsNull() throws Exception {
        new EmailBuilder()
                .setFrom(new InternetAddress(SENDER_EMAIL_ADDRESS, SENDER_NAME))
                .setSubject(SUBJECT)
                .setHtmlBody(HTML_BODY)
                .build();
    }

    @Test(expected = EmailCreationException.class)
    public void shouldThrowErrorIfSubjectIsNull() throws Exception {
        new EmailBuilder()
                .setFrom(new InternetAddress(SENDER_EMAIL_ADDRESS, SENDER_NAME))
                .setTo(new InternetAddress(RECEIVER_EMAIL_ADDRESS, RECEIVER_NAME))
                .setHtmlBody(HTML_BODY)
                .build();
    }

    @Test(expected = EmailCreationException.class)
    public void shouldThrowErrorIfHtmlBodyIsNull() throws Exception {
        new EmailBuilder()
                .setFrom(new InternetAddress(SENDER_EMAIL_ADDRESS, SENDER_NAME))
                .setTo(new InternetAddress(RECEIVER_EMAIL_ADDRESS, RECEIVER_NAME))
                .setSubject(SUBJECT)
                .build();
    }
}