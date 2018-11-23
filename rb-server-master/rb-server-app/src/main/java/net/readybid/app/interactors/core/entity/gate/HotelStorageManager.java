package net.readybid.app.interactors.core.entity.gate;

import net.readybid.entities.chain.HotelChain;

public interface HotelStorageManager {
    void setMasterChainBasicDetails(HotelChain chain);

    void setBrandBasicDetails(HotelChain chain);
}
