package net.readybid.app.core.transaction.exceptions;

/**
 * Created by DejanK on 9/13/2016.
 *
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(){super();}

    public NotFoundException(String message){
        super(message);
    }
}
