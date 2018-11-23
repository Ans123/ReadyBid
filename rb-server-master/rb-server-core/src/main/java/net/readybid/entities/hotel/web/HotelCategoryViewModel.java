package net.readybid.entities.hotel.web;

import net.readybid.app.core.entities.entity.hotel.HotelCategory;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 1/13/2017.
 *
 */
public class HotelCategoryViewModel implements ViewModel<HotelCategory> {

    public static final ViewModelFactory<HotelCategory, HotelCategoryViewModel> FACTORY = HotelCategoryViewModel::new;

    public final int id;
    public final String label;

    public HotelCategoryViewModel(HotelCategory hotelCategory) {
        id = hotelCategory.getId();
        label = hotelCategory.getLabel();
    }
}
