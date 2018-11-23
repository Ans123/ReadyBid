package net.readybid.exceptions;

/**
 * Created by DejanK on 9/13/2016.
 *
 */
public class BadRequestException extends RuntimeException {

    public String id;
    public String value;

    public BadRequestException() {}

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String id, String message, String value) {
        super(message);
        this.id = id;
        this.value = value;
    }

}
