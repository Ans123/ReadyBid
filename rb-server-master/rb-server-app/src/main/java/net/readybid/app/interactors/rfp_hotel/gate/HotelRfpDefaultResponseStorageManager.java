package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.app.entities.rfp_hotel.HotelRfpDefaultResponse;

import java.util.List;

public interface HotelRfpDefaultResponseStorageManager {
    void updateHotelData(Entity hotel);

    void setResponses(List<HotelRfpDefaultResponse> responses);
}
