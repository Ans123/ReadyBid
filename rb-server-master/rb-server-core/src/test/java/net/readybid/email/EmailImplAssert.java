package net.readybid.email;

import org.assertj.core.api.AbstractAssert;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Objects;

/**
 * Created by DejanK on 9/14/2016.
 *
 */
public class EmailImplAssert extends AbstractAssert<EmailImplAssert, EmailImpl> {
    public EmailImplAssert(EmailImpl actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public static EmailImplAssert that(EmailImpl actual) {
        return new EmailImplAssert(actual, EmailImplAssert.class);
    }
    public static EmailImplAssert that(Email actual) {
        if(!(actual instanceof EmailImpl)) throw new RuntimeException("Unsupported Class");
        return new EmailImplAssert((EmailImpl) actual, EmailImplAssert.class);
    }

    public EmailImplAssert hasSenderAddress(String senderEmailAddress) {
        try {
            isNotNull();
            final MimeMessage message = actual.getMimeMessage();
            final String actualEmailAddress =((InternetAddress) message.getFrom()[0]).getAddress();
            if (!Objects.equals(actualEmailAddress, senderEmailAddress)) {
                failWithMessage("Expected Email sender email address to be <%s> but was <%s>", senderEmailAddress, actualEmailAddress);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            failWithMessage("EmailImpl sender cannot be loaded");
        }
        return this;
    }

    public EmailImplAssert hasSenderName(String senderName) {
        try {
            isNotNull();
            final MimeMessage message = actual.getMimeMessage();
            final String actualName =((InternetAddress) message.getFrom()[0]).getPersonal();
            if (!Objects.equals(actualName, senderName)) {
                failWithMessage("Expected Email sender name to be <%s> but was <%s>", senderName, actualName);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            failWithMessage("EmailImpl sender cannot be loaded");
        }
        return this;
    }

    public EmailImplAssert hasReceiverAddress(String receiverEmailAddress) {
        try {
            isNotNull();
            final MimeMessage message = actual.getMimeMessage();
            final String actualEmailAddress =((InternetAddress) message.getAllRecipients()[0]).getAddress();
            if (!Objects.equals(actualEmailAddress, receiverEmailAddress)) {
                failWithMessage("Expected Email receiver email address to be <%s> but was <%s>", receiverEmailAddress, actualEmailAddress);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            failWithMessage("EmailImpl receiver cannot be loaded");
        }
        return this;
    }

    public EmailImplAssert hasReceiverName(String receiverName) {
        try {
            isNotNull();
            final MimeMessage message = actual.getMimeMessage();
            final String actualName =((InternetAddress) message.getAllRecipients()[0]).getPersonal();
            if (!Objects.equals(actualName, receiverName)) {
                failWithMessage("Expected Email receiver name to be <%s> but was <%s>", receiverName, actualName);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            failWithMessage("EmailImpl receiver cannot be loaded");
        }
        return this;
    }

    public EmailImplAssert hasSubject(String subject) {
        try {
            isNotNull();
            final String actualSubject = actual.getMimeMessage().getSubject();
            if (!Objects.equals(actualSubject, subject)) {
                failWithMessage("Expected Email subject to be <%s> but was <%s>", subject, actualSubject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            failWithMessage("EmailImpl subject cannot be loaded");
        }

        return this;
    }

    public EmailImplAssert hasHtmlBody(String htmlBody) {
        try {
            isNotNull();
            final MimeMessage message = actual.getMimeMessage();
            String actualBody = ((MimeMultipart) message.getContent()).getBodyPart(0).getContent().toString();
            if (!Objects.equals(actualBody, htmlBody)) {
                failWithMessage("Expected Email htmlBody to be <%s> but was <%s>", htmlBody, actualBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            failWithMessage("EmailImpl htmlBody cannot be loaded");
        }

        return this;
    }

    public EmailImplAssert hasSender(InternetAddress expected) {
        try {
            isNotNull();
            final MimeMessage message = actual.getMimeMessage();
            final InternetAddress actualSender = (InternetAddress) message.getFrom()[0];
            if (!Objects.equals(actualSender, expected)) {
                failWithMessage("Expected Email sender to be <%s> but was <%s>", expected, actualSender);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            failWithMessage("EmailImpl sender cannot be loaded");
        }
        return this;
    }

//    public EmailImplAssert hasTextBody(String textBody) {
//        try {
//            isNotNull();
//            final MimeMessage message = actual.getMimeMessage();
//            String actualBody = ((MimeMultipart) message.getContent()).getBodyPart(0).getContent().toString();
//            if (!Objects.equals(actualBody, textBody)) {
//                failWithMessage("Expected Email textBody to be <%s> but was <%s>", textBody, actualBody);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            failWithMessage("EmailImpl htmlBody cannot be loaded");
//        }
//
//        return this;
//    }
//
//    public EmailImplAssert hasAttachment(String attachment) {
//        try {
//            isNotNull();
//            final MimeMessage message = actual.getMimeMessage();
//            String actualBody = ((MimeMultipart) message.getContent()).getBodyPart(0).getContent().toString();
//            if (!Objects.equals(actualBody, attachment)) {
//                failWithMessage("Expected Email attachment to be <%s> but was <%s>", attachment, actualBody);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            failWithMessage("EmailImpl htmlBody cannot be loaded");
//        }
//        return this;
//    }
}