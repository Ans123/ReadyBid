package net.readybid.auth.user;

/**
 * Created by DejanK on 3/21/2017.
 *
 */
public enum UserStatus {
    ACTIVE, PENDING_EMAIL_VERIFICATION;

    public boolean isActive() {
        return this.equals(ACTIVE);
    }

}
