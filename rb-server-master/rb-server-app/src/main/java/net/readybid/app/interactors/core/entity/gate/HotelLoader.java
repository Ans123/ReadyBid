package net.readybid.app.interactors.core.entity.gate;

import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.entities.Id;

import java.util.List;

public interface HotelLoader {
    List<Hotel> getPropertyCodes(List<Id> hotelsIds);
}
