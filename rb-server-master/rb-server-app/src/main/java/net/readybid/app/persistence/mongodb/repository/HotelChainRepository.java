package net.readybid.app.persistence.mongodb.repository;

import net.readybid.entities.chain.HotelChain;
import org.bson.conversions.Bson;

public interface HotelChainRepository {
    void update(Bson filter, Bson update);

    HotelChain findOne(Bson filter, boolean includeUnvalidated);
}
