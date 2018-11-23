package net.readybid.email;

import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import org.assertj.core.api.AbstractAssert;
import org.mockito.ArgumentCaptor;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by DejanK on 10/06/2016.
 *
 */
public class MimeMessageAssert extends AbstractAssert<MimeMessageAssert, MimeMessage> {
    public MimeMessageAssert(MimeMessage actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public static MimeMessageAssert that(MimeMessage actual) {
        return new MimeMessageAssert(actual, MimeMessageAssert.class);
    }

    public static MimeMessageAssert that(ArgumentCaptor<SendRawEmailRequest> captor) throws MessagingException {
        final SendRawEmailRequest request = captor.getValue();
        final ByteBuffer buffer = request.getRawMessage().getData();
        final Session s = Session.getInstance(new Properties(), null);
        final MimeMessage mimeMessage =  new MimeMessage(s, new ByteArrayInputStream(buffer.array()));


        return new MimeMessageAssert(mimeMessage, MimeMessageAssert.class);
    }

    public MimeMessageAssert hasSenderAddress(String senderEmailAddress) {
        try {
            isNotNull();
            final String actualEmailAddress =((InternetAddress) actual.getFrom()[0]).getAddress();
            if (!Objects.equals(actualEmailAddress, senderEmailAddress)) {
                failWithMessage("Expected sender email address to be <%s> but was <%s>", senderEmailAddress, actualEmailAddress);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            failWithMessage("MimeMessage sender cannot be loaded");
        }
        return this;
    }

    public MimeMessageAssert hasSenderName(String senderName) {
        try {
            isNotNull();
            final String actualName =((InternetAddress) actual.getFrom()[0]).getPersonal();
            if (!Objects.equals(actualName, senderName)) {
                failWithMessage("Expected sender name to be <%s> but was <%s>", senderName, actualName);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            failWithMessage("MimeMessage sender cannot be loaded");
        }
        return this;
    }

    public MimeMessageAssert hasReceiverAddress(String receiverEmailAddress) {
        try {
            isNotNull();
            final String actualEmailAddress =((InternetAddress) actual.getAllRecipients()[0]).getAddress();
            if (!Objects.equals(actualEmailAddress, receiverEmailAddress)) {
                failWithMessage("Expected receiver email address to be <%s> but was <%s>", receiverEmailAddress, actualEmailAddress);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            failWithMessage("MimeMessage receiver cannot be loaded");
        }
        return this;
    }

    public MimeMessageAssert hasReceiverName(String receiverName) {
        try {
            isNotNull();
            final String actualName =((InternetAddress) actual.getAllRecipients()[0]).getPersonal();
            if (!Objects.equals(actualName, receiverName)) {
                failWithMessage("Expected receiver name to be <%s> but was <%s>", receiverName, actualName);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            failWithMessage("MimeMessage receiver cannot be loaded");
        }
        return this;
    }

    public MimeMessageAssert hasReceiverName(String firstName, String lastName) {
        return hasReceiverName(String.format("%s %s", firstName, lastName));
    }


    public MimeMessageAssert hasSubject(String subject) {
        try {
            isNotNull();
            final String actualSubject = actual.getSubject();
            if (!Objects.equals(actualSubject, subject)) {
                failWithMessage("Expected subject to be <%s> but was <%s>", subject, actualSubject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            failWithMessage("MimeMessage subject cannot be loaded");
        }

        return this;
    }

    public MimeMessageAssert hasHtmlBody(String htmlBody) {
        try {
            isNotNull();
            String actualBody = ((MimeMultipart) actual.getContent()).getBodyPart(0).getContent().toString();
            if (!Objects.equals(actualBody, htmlBody)) {
                failWithMessage("Expected htmlBody to be <%s> but was <%s>", htmlBody, actualBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            failWithMessage("MimeMessage htmlBody cannot be loaded");
        }

        return this;
    }

    public MimeMessageAssert hasHtmlTemplateId(String templateId) {
        return hasInHtml(String.format(" id=\"templates-%s\"", templateId));
    }

    public MimeMessageAssert hasInHtml(String expectedText) {
        try {
            isNotNull();
            final String actualBody = ((MimeMultipart) actual.getContent()).getBodyPart(0).getContent().toString();
            if (!actualBody.contains(expectedText)) {
                failWithMessage("Expected htmlBody to contain <%s> but it doesn't", expectedText);
            }
        } catch (Exception e) {
            e.printStackTrace();
            failWithMessage("MimeMessage htmlBody cannot be loaded");
        }
        return this;
    }
}