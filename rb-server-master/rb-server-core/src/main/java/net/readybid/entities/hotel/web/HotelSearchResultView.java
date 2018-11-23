package net.readybid.entities.hotel.web;


import net.readybid.app.core.entities.entity.hotel.Hotel;
import net.readybid.entities.chain.HotelChainViewModel;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 12/25/2016.
 *
 */
public class HotelSearchResultView implements ViewModel<Hotel> {

    public static final ViewModelFactory<Hotel, HotelSearchResultView> FACTORY = HotelSearchResultView::new;

    public String id;
    public String name;
    public HotelChainViewModel chain;
    public String fullAddress;

    public HotelSearchResultView() {}

    public HotelSearchResultView(Hotel hotel) {
        id = hotel.getId().toString();
        name = hotel.getName();
        chain = HotelChainViewModel.FACTORY.createAsPartial(hotel.getChain());
        fullAddress = hotel.getFullAddress();
    }
}
