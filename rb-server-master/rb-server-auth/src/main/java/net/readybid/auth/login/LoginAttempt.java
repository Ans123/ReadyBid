package net.readybid.auth.login;

import org.bson.types.ObjectId;

/**
 * Created by DejanK on 3/22/2017.
 *
 */
public class LoginAttempt {
    public ObjectId id;
    public String target;
    public long at;

    public LoginAttempt() {}

    public LoginAttempt(String hashedTarget, long at) {
        id = new ObjectId();
        target = hashedTarget;
        this.at = at;
    }
}
