package net.readybid.rfphotel.supplier;

import net.readybid.app.core.entities.entity.EntityImage;
import net.readybid.app.core.entities.entity.hotel.HotelCategory;
import net.readybid.entities.chain.HotelChain;
import net.readybid.rfp.company.RfpCompanyImpl;

import java.util.List;

/**
 * Created by DejanK on 4/9/2017.
 *
 */
public class RfpHotelImpl extends RfpCompanyImpl implements RfpHotel {

    private HotelChain chain;
    private HotelCategory category;
    private int rating;
    private List<String> amenities;
    private EntityImage image;

    @Override
    public EntityImage getImage() {
        return image;
    }

    @Override
    public int getRating() {
        return rating;
    }

    @Override
    public HotelChain getChain() {
        return chain;
    }

    @Override
    public List<String> getAmenities() {
        return amenities;
    }

    @Override
    public HotelCategory getCategory() {
        return category;
    }

    @Override
    public String getBrandChainName() {
        return chain == null ? null : chain.getName();
    }

    @Override
    public String getMasterChainName() {
        return chain == null ? null : chain.getMasterChainName();
    }

    @Override
    public String getMasterChainId() {
        return chain == null ? null : chain.getMasterChainId();
    }

    public void setChain(HotelChain chain) {
        this.chain = chain;
    }

    public void setCategory(HotelCategory category) {
        this.category = category;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public void setImage(EntityImage image) {
        this.image = image;
    }
}
