package net.readybid.bidmanagerview;

import net.readybid.rfp.type.RfpType;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 1/28/2017.
 *
 */
public class BidManagerViewListItemViewModel implements ViewModel<BidManagerView> {

    public static final ViewModelFactory<BidManagerView, BidManagerViewListItemViewModel> FACTORY = BidManagerViewListItemViewModel::new;

    public ObjectId id;
    public String name;
    public final ObjectId rfpId;
    public final BidManagerViewType type;
    public final RfpType rfpType;
    public final BidManagerViewSide side;


    public BidManagerViewListItemViewModel(BidManagerView bidManagerView) {
        this.id = bidManagerView.getId();
        this.name = bidManagerView.getName();
        this.type = bidManagerView.getType();
        this.rfpId = bidManagerView.getRfpId();
        this.rfpType = bidManagerView.getRfpType();
        this.side = bidManagerView.getSide();
    }
}
