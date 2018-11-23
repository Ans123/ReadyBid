package net.readybid.exceptions;

public class UnrecoverableException extends RuntimeException {

    public UnrecoverableException(Exception e) {
        e.printStackTrace();
    }
}
