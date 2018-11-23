package net.readybid.utils;

import net.readybid.user.BasicUserDetails;
import net.readybid.user.BasicUserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public abstract class StatusDetails<T> {

    protected T value;
    protected BasicUserDetails by;
    protected Date at;

    public StatusDetails(){}

    // todo @PersistenceConstructor
    public StatusDetails(@Value("by") BasicUserDetailsImpl by) {
        this.by = by;
    }

    public StatusDetails(CreationDetails created, T statusValue) {
        value = statusValue;
        if(created != null){
            by = created.getBy();
            at = created.getAt();
        } else {
            at = new Date();
        }
    }

    public StatusDetails(BasicUserDetails by, T statusValue) {
        this(statusValue);
        this.by = by;
    }

    public StatusDetails(T statusValue) {
        value = statusValue;
        at = new Date();
    }

    public void setAt(Date at) {
        this.at = at;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setBy(BasicUserDetailsImpl by) {
        this.by = by;
    }

    public Date getAt() {
        return at;
    }

    public T getValue() {
        return value;
    }

    public BasicUserDetails getBy() {
        return by;
    }
}
