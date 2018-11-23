package net.readybid.entities.hotel.core;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.app.core.entities.entity.hotel.HotelCategory;
import net.readybid.entities.chain.HotelChain;
import net.readybid.entities.core.EntityImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public class HotelImpl extends EntityImpl implements Hotel {

    private HotelChain chain;
    private int rating;
    private List<String> amenities;
    private HotelCategory category;
    private Map<String, String> answers;

    public HotelImpl(){
        setType(EntityType.HOTEL);
    }

    public void setChain(HotelChain chain) {
        this.chain = chain;
    }

    @Override
    public HotelChain getChain() {
        return chain;
    }

    @Override
    public int getRating() {
        return rating;
    }

    @Override
    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setCategory(HotelCategory category) {
        this.category = category;
    }

    public HotelCategory getCategory() {
        return category;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }
}
