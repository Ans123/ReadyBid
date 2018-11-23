package net.readybid.rfp.bidmanagerview;

import net.readybid.bidmanagerview.BidManagerView;
import net.readybid.rfp.core.Rfp;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 1/28/2017.
 *
 */
public interface BidManagerViewFactory {
    BidManagerView createRfpViewForGuest(String viewName, Rfp rfp);

    String createNameForRfpView(String rfpName);

    List<BidManagerView> createMockedOtherRfpTypeViews(ObjectId guestId);

    BidManagerView createDefaultViewForGuest(ObjectId id);
}
