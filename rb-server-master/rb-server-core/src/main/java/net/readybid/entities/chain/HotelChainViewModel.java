package net.readybid.entities.chain;

import net.readybid.entities.core.EntityView;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 2/15/2017.
 */
public class HotelChainViewModel  extends EntityView implements ViewModel<HotelChain> {

    public String code;
    public HotelChainType subtype;
    public HotelChainViewModel masterChain;

    public static ViewModelFactory<HotelChain, HotelChainViewModel> FACTORY = HotelChainViewModel::new;

    public HotelChainViewModel() {}

    public HotelChainViewModel(HotelChain chain) {
        super(chain);

        code = chain.getCode();
        subtype = chain.getSubtype();
        masterChain = HotelChainViewModel.FACTORY.createAsPartial(chain.getMasterChain());
    }
}