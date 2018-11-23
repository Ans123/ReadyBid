package net.readybid.entities.hotel.web;


import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.entities.chain.HotelChainViewModel;
import net.readybid.entities.core.EntityView;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

import java.util.List;

/**
 * Created by DejanK on 12/24/2016.
 *
 */
public class HotelViewModel extends EntityView implements ViewModel<Hotel> {

    public static ViewModelFactory<Hotel, HotelViewModel> FACTORY = HotelViewModel::new;

    public int rating;
    public HotelChainViewModel chain;
    public List<String> amenities;
    public HotelCategoryViewModel category;
    public HotelGdsCodesView gdsCodes;

    public HotelViewModel(Hotel hotel) {
        super(hotel);
        rating = hotel.getRating();
        chain = HotelChainViewModel.FACTORY.createAsPartial(hotel.getChain());
        amenities = hotel.getAmenities();
        category = HotelCategoryViewModel.FACTORY.createAsPartial((hotel.getCategory()));
        gdsCodes = new HotelGdsCodesView(hotel.getAnswers(), hotel.getChain());
    }

    public HotelViewModel() {
        super();
    }
}
