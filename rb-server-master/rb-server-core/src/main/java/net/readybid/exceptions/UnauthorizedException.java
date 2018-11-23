package net.readybid.exceptions;

/**
 * Created by DejanK on 3/22/2017.
 *
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(){super();}

    public UnauthorizedException(String message){
        super(message);
    }
}