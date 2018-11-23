package net.readybid.entities.chain;


import net.readybid.web.ViewModel;

/**
 * Created by DejanK on 1/19/2017.
 *
 */
public class HotelChainListItemViewModel implements ViewModel {

    public String id;
    public String name;
    public String code;
    public HotelChainType subtype;
    public HotelChainListItemViewModel masterChain;
}
