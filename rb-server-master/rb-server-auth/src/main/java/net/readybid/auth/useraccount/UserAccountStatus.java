package net.readybid.auth.useraccount;

/**
 * Created by DejanK on 3/28/2017.
 *
 */
public enum UserAccountStatus {
    UNVERIFIED,
    ACTIVE;

    public boolean isActive() {
        return this.equals(ACTIVE);
    }
}
