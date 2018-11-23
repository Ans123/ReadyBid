package net.readybid.entities.hotel.logic;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
public interface HotelRepository {

    Hotel findById(String id);

    Hotel findByIdIncludingUnverified(String id);

    void saveForValidation(Hotel hotel);

    Hotel findByIdIncludingUnverified(ObjectId id, String... fields);

    Entity findByIdIncludingUnverified(String id, String... fields);
}
