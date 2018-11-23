package net.readybid.entities.core;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public enum EntityStatus {
    PENDING,
    PRELOADED,
    ACTIVE;

    public boolean isActive() {
        return this.equals(ACTIVE) || this.equals(PRELOADED);
    }
}
