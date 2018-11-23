package net.readybid.entities.hmc;

import net.readybid.entities.core.EntityView;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 2/15/2017.
 *
 */
public class HotelManagementCompanyViewModel extends EntityView implements ViewModel<HotelManagementCompany> {

    public static ViewModelFactory<HotelManagementCompany, HotelManagementCompanyViewModel> FACTORY = HotelManagementCompanyViewModel::new;

    public HotelManagementCompanyViewModel(HotelManagementCompany company) {
        super(company);
    }
}
