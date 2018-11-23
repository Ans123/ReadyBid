package net.readybid.rfphotel.supplier;

import net.readybid.app.core.entities.entity.EntityIndustry;
import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.entities.chain.HotelChainViewModel;
import net.readybid.entities.core.EntityImageView;
import net.readybid.entities.hotel.web.HotelCategoryViewModel;
import net.readybid.entities.hotel.web.HotelViewModel;
import net.readybid.location.LocationViewModel;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 4/9/2017.
 *
 */
public class RfpHotelViewModel implements ViewModel<RfpHotel>{

    public static final ViewModelFactory<RfpHotel, RfpHotelViewModel> FACTORY = RfpHotelViewModel::new;

    public ObjectId accountId;
    public ObjectId entityId;
    public String name;
    public EntityType type;
    public EntityIndustry industry;
    public LocationViewModel location;
    public String website;
    public String logo;
    public String emailAddress;
    public String phone;

    public int rating;
    public HotelChainViewModel chain;
    public List<String> amenities;
    public HotelCategoryViewModel category;
    public EntityImageView image;

    public RfpHotelViewModel(RfpHotel rfpHotel) {
        accountId = rfpHotel.getAccountId();
        entityId = rfpHotel.getEntityId();
        name = rfpHotel.getName();
        type = rfpHotel.getType();
        location = LocationViewModel.FACTORY.createAsPartial(rfpHotel.getLocation());
        website = rfpHotel.getWebsite();
        logo = rfpHotel.getLogo();
        emailAddress = rfpHotel.getEmailAddress();
        phone = rfpHotel.getPhone();

        rating = rfpHotel.getRating();
        chain = HotelChainViewModel.FACTORY.createAsPartial(rfpHotel.getChain());
        amenities = rfpHotel.getAmenities();
        category = HotelCategoryViewModel.FACTORY.createAsPartial((rfpHotel.getCategory()));
        image = EntityImageView.FACTORY.createAsPartial(rfpHotel.getImage());
    }

    public static HotelViewModel createAsPartial(RfpHotel rfpHotel) {
        final HotelViewModel hotelViewModel = new HotelViewModel();

        hotelViewModel.id = String.valueOf(rfpHotel.getEntityId());
        hotelViewModel.type = rfpHotel.getType();
        hotelViewModel.name = rfpHotel.getName();
        hotelViewModel.industry = rfpHotel.getIndustry();
        hotelViewModel.website = rfpHotel.getWebsite();
        hotelViewModel.emailAddress = rfpHotel.getEmailAddress();
        hotelViewModel.phone = rfpHotel.getPhone();
        hotelViewModel.logo = rfpHotel.getLogo();
        hotelViewModel.image = EntityImageView.FACTORY.createAsPartial(rfpHotel.getImage());
        hotelViewModel.location = LocationViewModel.FACTORY.createAsPartial(rfpHotel.getLocation());

        hotelViewModel.rating = rfpHotel.getRating();
        hotelViewModel.chain = HotelChainViewModel.FACTORY.createAsPartial(rfpHotel.getChain());
        hotelViewModel.amenities = rfpHotel.getAmenities();
        hotelViewModel.category = HotelCategoryViewModel.FACTORY.createAsPartial((rfpHotel.getCategory()));

        return hotelViewModel;
    }
}
