package net.readybid.email;

/**
 * Created by DejanK on 8/27/2015.
 *
 */
public class EmailCreationException extends RuntimeException {

    // TODO: LOG
    public EmailCreationException(Exception e) {
        e.printStackTrace();
    }

    public EmailCreationException(String s) {

    }
}
