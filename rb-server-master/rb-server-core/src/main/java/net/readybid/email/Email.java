package net.readybid.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by DejanK on 10/4/2016.
 *
 */
public interface Email {
    MimeMessage getMimeMessage() throws MessagingException;
}
