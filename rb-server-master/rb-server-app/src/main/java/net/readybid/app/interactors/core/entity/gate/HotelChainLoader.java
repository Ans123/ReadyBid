package net.readybid.app.interactors.core.entity.gate;

import net.readybid.entities.chain.HotelChain;

public interface HotelChainLoader {
    HotelChain load(String id);
}
