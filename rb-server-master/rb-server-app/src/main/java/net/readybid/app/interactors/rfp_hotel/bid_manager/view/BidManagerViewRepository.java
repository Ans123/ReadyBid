package net.readybid.app.interactors.rfp_hotel.bid_manager.view;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.bidmanagerview.BidManagerView;
import net.readybid.bidmanagerview.BidManagerViewListItemViewModel;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
public interface BidManagerViewRepository {
    void createView(BidManagerView view);

    BidManagerView findViewByNameForUser(String viewName, AuthenticatedUser user);

    void updateViewName(String rfpId, String viewName);

    BidManagerView findViewById(String viewId, AuthenticatedUser user);

    BidManagerView findViewByRfpIdAndOwner(String rfpId, AuthenticatedUser user);

    ListResult<BidManagerViewListItemViewModel> listViews(AuthenticatedUser user);
}
