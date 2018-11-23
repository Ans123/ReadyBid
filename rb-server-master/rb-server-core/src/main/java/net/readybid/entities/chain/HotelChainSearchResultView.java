package net.readybid.entities.chain;


import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
public class HotelChainSearchResultView implements ViewModel<HotelChain> {

    public static final ViewModelFactory<HotelChain, HotelChainSearchResultView> FACTORY = HotelChainSearchResultView::new;

    public String id;
    public String name;
    public String fullAddress;

    public HotelChainSearchResultView(HotelChain chain) {
        id = chain.getId().toString();
        name = chain.getName();
    }

    public HotelChainSearchResultView() {}

}
