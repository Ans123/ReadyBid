package net.readybid.api.main.bidmanager.core;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.bidmanagerview.BidManagerView;
import net.readybid.bidmanagerview.BidManagerViewListItemViewModel;
import net.readybid.rfphotel.destinations.web.TravelDestinationListItemViewModel;
import net.readybid.utils.ListResult;

/**
 * Created by DejanK on 4/5/2017.
 *
 */
public interface BidManagerFacade {
    ListResult<BidManagerViewListItemViewModel> listUserViews(AuthenticatedUser user);

    BidManagerView getUserView(String viewId, AuthenticatedUser user);

    BidManagerView getUserViewByRfpId(String rfpId, AuthenticatedUser user);

    ListResult<TravelDestinationListItemViewModel> listUserTravelDestinations(AuthenticatedUser user);

    ListResult<TravelDestinationListItemViewModel> listRfpTravelDestinations(String rfpId);
}
