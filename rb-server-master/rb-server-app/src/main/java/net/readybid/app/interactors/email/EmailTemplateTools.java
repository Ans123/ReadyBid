package net.readybid.app.interactors.email;

import net.readybid.email.EmailCreationException;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

public class EmailTemplateTools {

    private EmailTemplateTools(){}

    public static InternetAddress createInternetAddress(String emailAddress){
        return createInternetAddress(emailAddress, emailAddress);
    }

    public static InternetAddress createInternetAddress(String emailAddress, String name) {
        try {
            return new InternetAddress(emailAddress, name);
        } catch (UnsupportedEncodingException e){
            throw new EmailCreationException(e);
        }
    }
}
