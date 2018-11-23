package net.readybid.email;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * Created by DejanK on 8/27/2015.
 *
 */
class EmailImpl implements Email {

    InternetAddress from;
    InternetAddress to;
    InternetAddress[] ccs;
    String subject;
    String htmlBody;
//    String textBody;
//    final ArrayList<String> attachments = new ArrayList<>();

    @Override
    public MimeMessage getMimeMessage() throws MessagingException {
        final Session s = Session.getInstance(new Properties(), null);
        final MimeMessage mimeMessage =  new MimeMessage(s);
        mimeMessage.setFrom(from);
        mimeMessage.setRecipient(Message.RecipientType.TO, to);
        if(ccs != null && ccs.length > 0){
            mimeMessage.setRecipients(Message.RecipientType.CC, ccs);
        }
        mimeMessage.setSubject(subject);

        final MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(createBody());
//            addAttachments(mp);

        mimeMessage.setContent(mp);
        return mimeMessage;
    }

//    private void addAttachments(MimeMultipart mp) throws MessagingException {
//        BodyPart attachment;
//        for (String filename : attachments) {
//            attachment = new MimeBodyPart();
//            DataSource source = new FileDataSource(filename);
//            attachment.setDataHandler(new DataHandler(source));
//            attachment.setFileName(source.getName());
//            mp.addBodyPart(attachment);
//        }
//    }

    private BodyPart createBody() throws MessagingException {
        BodyPart part = new MimeBodyPart();
        part.setContent(htmlBody, "text/html");
        return part;
    }
}
