package net.readybid.api.main.bidmanager.core;

import net.readybid.api.main.access.RfpAccessControlService;
import net.readybid.app.interactors.rfp_hotel.bid_manager.view.BidManagerViewService;
import net.readybid.api.main.rfp.destinations.TravelDestinationService;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.bidmanagerview.BidManagerView;
import net.readybid.bidmanagerview.BidManagerViewListItemViewModel;
import net.readybid.rfphotel.destinations.web.TravelDestinationListItemViewModel;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 4/5/2017.
 *
 */
@Service
public class BidManagerFacadeImpl implements BidManagerFacade {

    private final BidManagerViewService bidManagerViewService;
    private final RfpAccessControlService rfpAccessControlService;
    private final TravelDestinationService travelDestinationService;

    @Autowired
    public BidManagerFacadeImpl(BidManagerViewService bidManagerViewService, RfpAccessControlService rfpAccessControlService, TravelDestinationService travelDestinationService) {
        this.bidManagerViewService = bidManagerViewService;
        this.rfpAccessControlService = rfpAccessControlService;
        this.travelDestinationService = travelDestinationService;
    }

    @Override
    public ListResult<BidManagerViewListItemViewModel> listUserViews(AuthenticatedUser user) {
        return bidManagerViewService.listUserViews(user);
    }

    @Override
    public BidManagerView getUserView(String viewId, AuthenticatedUser user) {
        return bidManagerViewService.getUserView(viewId, user);
    }

    @Override
    public BidManagerView getUserViewByRfpId(String rfpId, AuthenticatedUser user) {
        return bidManagerViewService.getUserViewByRfpId(rfpId, user);
    }

    @Override
    public ListResult<TravelDestinationListItemViewModel> listUserTravelDestinations(AuthenticatedUser user) {
        return travelDestinationService.listUserTravelDestinationsWithRfpName(user);
    }

    @Override
    public ListResult<TravelDestinationListItemViewModel> listRfpTravelDestinations(String rfpId) {
        rfpAccessControlService.read(rfpId);
        return travelDestinationService.listRfpTravelDestinationsWithRfpName(rfpId);
    }
}
