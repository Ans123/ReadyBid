package net.readybid.email;

/**
 * Created by DejanK on 8/27/2015.
 *
 */
class EmailSendingFailedException extends RuntimeException {

    // TODO: Log
    public EmailSendingFailedException(Exception e) {
        System.out.println("Email sending failed!");
        e.printStackTrace();
    }
}
