package net.readybid.email;

import javax.mail.internet.InternetAddress;

/**
 * Created by DejanK on 10/4/2016.
 *
 */
public class EmailBuilder {

    final EmailImpl email;

    public EmailBuilder(){
        email = new EmailImpl();
    }

    public EmailBuilder setFrom(InternetAddress from){
        email.from = from;
        return this;
    }

    public EmailBuilder setTo(InternetAddress to){
        email.to = to;
        return this;
    }

    public EmailBuilder setSubject(String subject){
        email.subject = subject;
        return this;
    }

    public EmailBuilder setHtmlBody(String htmlBody){
        email.htmlBody = htmlBody;
        return this;
    }

//    public EmailBuilder setTextBody(String textBody){
//        email.textBody = textBody;
//        return this;
//    }

//    public EmailBuilder addAttachment(String attachmentFile){
//        email.attachments.add(attachmentFile);
//        return this;
//    }

    public Email build(){
        if(email.from == null || email.to == null || email.subject == null || email.htmlBody == null){
            throw new EmailCreationException("Email not complete");
        }
        return email;
    }

    public EmailBuilder setCC(InternetAddress[] CC) {
        email.ccs = CC;
        return this;
    }
}