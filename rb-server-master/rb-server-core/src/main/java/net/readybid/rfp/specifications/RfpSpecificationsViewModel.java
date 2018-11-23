package net.readybid.rfp.specifications;

import net.readybid.rfp.buyer.BuyerViewModel;
import net.readybid.rfp.type.RfpType;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class RfpSpecificationsViewModel implements ViewModel<RfpSpecifications> {

    public static ViewModelFactory<RfpSpecifications, RfpSpecificationsViewModel> FACTORY = RfpSpecificationsViewModel::new;

    public String name;
    public RfpType type;
    public boolean chainSupport = false;
    public String dueDate;
    public String programStartDate;
    public String programEndDate;
    public int programYear;
    public BuyerViewModel buyer;

    public RfpSpecificationsViewModel(RfpSpecifications rfpSpecifications){
        name = rfpSpecifications.getName();
        type = rfpSpecifications.getType();
        chainSupport = rfpSpecifications.isChainSupportEnabled();
        dueDate = rfpSpecifications.getDueDate() == null ? null : rfpSpecifications.getDueDate().toString();
        programStartDate = rfpSpecifications.getProgramStartDate() == null ? null : rfpSpecifications.getProgramStartDate().toString();
        programEndDate = rfpSpecifications.getProgramEndDate() == null ? null : rfpSpecifications.getProgramEndDate().toString();
        programYear = rfpSpecifications.getProgramYear();
        buyer = BuyerViewModel.FACTORY.createAsPartial(rfpSpecifications.getBuyer());
    }
}
