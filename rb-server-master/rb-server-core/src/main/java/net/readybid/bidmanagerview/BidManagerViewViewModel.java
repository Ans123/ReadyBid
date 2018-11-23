package net.readybid.bidmanagerview;

import net.readybid.rfp.type.RfpType;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by DejanK on 1/28/2017.
 *
 */
public class BidManagerViewViewModel implements ViewModel<BidManagerView> {

    public static ViewModelFactory<BidManagerView, BidManagerViewViewModel> FACTORY = BidManagerViewViewModel::new;;

    public final ObjectId id;
    public final ObjectId rfpId;
    public final BidManagerViewType type;
    public final RfpType rfpType;
    public final BidManagerViewSide side;
    public final String name;
    public final List<String> columns;
    public final Map<String, Object> filter;
    public final Map<String, Object> sort;
    public final String group;
    public final ObjectId owner;

    public BidManagerViewViewModel(BidManagerView view) {
        this.id = view.getId();
        this.type = view.getType();
        this.rfpId = view.getRfpId();
        this.rfpType = view.getRfpType();
        this.side = view.getSide();
        this.name = view.getName();
        this.columns = view.getColumns();
        this.filter = view.getFilter();
        this.sort = view.getSort();
        this.group = view.getGroup();
        this.owner = view.getOwner();

    }
}
