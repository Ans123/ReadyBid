package net.readybid.app.persistence.mongodb.repository;

import net.readybid.app.core.entities.entity.hotel.Hotel;
import org.bson.conversions.Bson;

import java.util.List;

public interface HotelRepository {
    void update(Bson filter, Bson update);

    List<? extends Hotel> find(Bson filter, Bson projection);
}
