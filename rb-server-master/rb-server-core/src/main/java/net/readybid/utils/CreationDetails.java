package net.readybid.utils;

import net.readybid.user.BasicUserDetails;
import net.readybid.user.BasicUserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public class CreationDetails {

    private BasicUserDetails by;
    private Date at;

    public CreationDetails() {}

    // todo @PersistenceConstructor
    public CreationDetails(@Value("by") BasicUserDetailsImpl by) {
        this.by = by;
    }

    public CreationDetails(BasicUserDetails user) {
        by = user;
        at = new Date();
    }

    public void setAt(Date at) {
        this.at = at;
    }

    public void setBy(BasicUserDetailsImpl by) {
        this.by = by;
    }

    public Date getAt() {
        return at;
    }

    public BasicUserDetails getBy() {
        return by;
    }
}
