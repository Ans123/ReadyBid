package net.readybid.entities.hmc;


import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
public class HotelManagementCompanyFacadeSearchResultView implements ViewModel<HotelManagementCompany> {

    public static final ViewModelFactory<HotelManagementCompany, HotelManagementCompanyFacadeSearchResultView> FACTORY =
            HotelManagementCompanyFacadeSearchResultView::new;

    public String id;
    public String name;
    public String fullAddress;

    public HotelManagementCompanyFacadeSearchResultView(HotelManagementCompany company) {
        id = company.getId().toString();
        name = company.getName();
        fullAddress = company.getFullAddress();
    }

    public HotelManagementCompanyFacadeSearchResultView() {}

}
