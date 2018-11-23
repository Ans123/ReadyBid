package net.readybid.app.core.entities.entity.hotel;

import net.readybid.app.core.entities.entity.Entity;
import net.readybid.entities.chain.HotelChain;

import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public interface Hotel extends Entity {

    HotelChain getChain();

    int getRating();

    List<String> getAmenities();

    HotelCategory getCategory();

    Map<String, String> getAnswers();
}
