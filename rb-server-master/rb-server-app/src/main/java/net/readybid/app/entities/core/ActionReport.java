package net.readybid.app.entities.core;

public class ActionReport<T> {

    public final Status status;
    public final String message;
    public final T tObject;

    public ActionReport(Status status, String message, T tObject) {
        this.status = status;
        this.message = message;
        this.tObject = tObject;
    }

    public enum Status {
        OK,
        NOT_FOUND,
        STATUS_CHANGED,
        ERROR
    }
}
