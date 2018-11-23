package net.readybid.rfp.buyer;


import net.readybid.rfp.company.RfpCompanyViewModel;
import net.readybid.rfp.contact.RfpContactViewModel;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class BuyerViewModel implements ViewModel<Buyer> {

    public static final ViewModelFactory<Buyer, BuyerViewModel> FACTORY = BuyerViewModel::new;

    public RfpCompanyViewModel company;
    public RfpContactViewModel contact;

    public BuyerViewModel(Buyer buyer) {
        company = RfpCompanyViewModel.FACTORY.createAsPartial(buyer.getCompany());
        contact = RfpContactViewModel.FACTORY.createAsPartial(buyer.getContact());
    }
}
