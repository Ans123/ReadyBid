package net.readybid.app.core.service;

import net.readybid.app.core.entities.entity.hotel.Hotel;

public interface LoadHotelRepository {
    Hotel getAnswersById(String hotelId);
}
