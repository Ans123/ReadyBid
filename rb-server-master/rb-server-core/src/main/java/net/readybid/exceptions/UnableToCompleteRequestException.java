package net.readybid.exceptions;

/**
 * Created by DejanK on 11/23/2016.
 *
 */
public class UnableToCompleteRequestException extends RuntimeException {
    public String message;

    public UnableToCompleteRequestException(String message) {

        this.message = message;
    }
}
