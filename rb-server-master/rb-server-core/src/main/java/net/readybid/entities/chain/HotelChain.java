package net.readybid.entities.chain;

import net.readybid.app.core.entities.entity.Entity;

/**
 * Created by DejanK on 12/20/2016.
 *
 */
public interface HotelChain extends Entity {
    String getCode();

    HotelChainType getSubtype();

    HotelChain getMasterChain();

    String getMasterChainName();

    boolean isMasterChain();

    String getMasterChainId();
}
