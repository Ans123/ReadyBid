package net.readybid.app.interactors.rfp_hotel.bid_manager.view;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.bidmanagerview.BidManagerView;
import net.readybid.bidmanagerview.BidManagerViewListItemViewModel;
import net.readybid.rfp.core.Rfp;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
public interface BidManagerViewService {
    void createRfpView(Rfp rfp, AuthenticatedUser user);

    void updateRfpViewName(String rfpId, String name, AuthenticatedUser user);

    BidManagerView getUserView(String viewId, AuthenticatedUser user);

    BidManagerView getUserViewByRfpId(String rfpId, AuthenticatedUser user);

    ListResult<BidManagerViewListItemViewModel> listUserViews(AuthenticatedUser user);
}
