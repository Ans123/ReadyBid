package net.readybid.api.main.bid.supplier;

import net.readybid.app.interactors.rfp_hotel.dirty.HotelRfpBidBuilder;
import net.readybid.app.interactors.rfp_hotel.dirty.RfpHotelFactory;
import net.readybid.auth.account.core.Account;
import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.rfphotel.supplier.RfpHotel;
import net.readybid.rfphotel.supplier.RfpHotelImpl;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 4/9/2017.
 *
 */
@Service
public class RfpHotelFactoryImpl implements RfpHotelFactory {

    @Override
    public RfpHotel create(HotelRfpBidBuilder hotelRfpBidBuilder) {
        final Hotel hotel = hotelRfpBidBuilder.getHotel();
        final Account account = hotelRfpBidBuilder.getAccount();
        return create(hotel, account);
    }

    @Override
    public RfpHotel create(Hotel hotel, Account account) {
        final RfpHotelImpl rfpHotel = new RfpHotelImpl();

        rfpHotel.setEntityId(hotel.getId());
        rfpHotel.setAccountId(account.getId());

        rfpHotel.setName(hotel.getName());
        rfpHotel.setType(hotel.getType());
        rfpHotel.setIndustry(hotel.getIndustry());
        rfpHotel.setLocation(hotel.getLocation());
        rfpHotel.setWebsite(hotel.getWebsite());
        rfpHotel.setLogo(hotel.getLogo());
        rfpHotel.setEmailAddress(hotel.getEmailAddress());
        rfpHotel.setPhone(hotel.getPhone());
        rfpHotel.setChain(hotel.getChain());
        rfpHotel.setCategory(hotel.getCategory());
        rfpHotel.setRating(hotel.getRating());
        rfpHotel.setAmenities(hotel.getAmenities());
        rfpHotel.setImage(hotel.getImage());

        return rfpHotel;
    }
}
