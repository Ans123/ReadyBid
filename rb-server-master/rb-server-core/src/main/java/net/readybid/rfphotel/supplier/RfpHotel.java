package net.readybid.rfphotel.supplier;

import net.readybid.app.core.entities.entity.EntityImage;
import net.readybid.app.core.entities.entity.hotel.HotelCategory;
import net.readybid.entities.chain.HotelChain;
import net.readybid.rfp.company.RfpCompany;

import java.util.List;

/**
 * Created by DejanK on 4/9/2017.
 *
 */
public interface RfpHotel extends RfpCompany {

    EntityImage getImage();

    int getRating();

    HotelChain getChain();

    List<String> getAmenities();

    HotelCategory getCategory();

    String getBrandChainName();

    String getMasterChainName();

    String getMasterChainId();
}
