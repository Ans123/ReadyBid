package net.readybid.api.main.bidmanager.web;

import net.readybid.api.main.bidmanager.core.BidManagerFacade;
import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.auth.user.CurrentUser;
import net.readybid.bidmanagerview.BidManagerView;
import net.readybid.bidmanagerview.BidManagerViewListItemViewModel;
import net.readybid.bidmanagerview.BidManagerViewViewModel;
import net.readybid.utils.ListResult;
import net.readybid.web.GetResponse;
import net.readybid.web.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by DejanK on 1/2/2017.
 *
 */
@RestController
@RequestMapping(value = "/bid-managers")
public class BidManagerController {

    private final BidManagerFacade bidManagerFacade;

    @Autowired
    public BidManagerController(BidManagerFacade bidManagerFacade) {
        this.bidManagerFacade = bidManagerFacade;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ListResponse<BidManagerViewListItemViewModel> getBidManagerViews(
            @CurrentUser AuthenticatedUser user
    ) {
        final ListResponse<BidManagerViewListItemViewModel> response = new ListResponse<>();
        final ListResult<BidManagerViewListItemViewModel> views = bidManagerFacade.listUserViews(user);
        return response.finalizeResult(views);
    }

    @RequestMapping(value = "/{viewId}", method = RequestMethod.GET)
    public GetResponse<BidManagerView, BidManagerViewViewModel> getBidManagerView( @PathVariable(value = "viewId") String viewId, @CurrentUser AuthenticatedUser user    ) 
    {
        final GetResponse<BidManagerView, BidManagerViewViewModel> response = new GetResponse<>();
        final BidManagerView view = bidManagerFacade.getUserView(viewId, user);
        return response.finalizeResult(view, BidManagerViewViewModel.FACTORY);
    }

    @RequestMapping(value = "/rfps/{rfpId}", method = RequestMethod.GET)
    public GetResponse<BidManagerView, BidManagerViewViewModel> getBidManagerViewByRfpId(
            @PathVariable(value = "rfpId") String rfpId,
            @CurrentUser AuthenticatedUser user
    ) {
        final GetResponse<BidManagerView, BidManagerViewViewModel> response = new GetResponse<>();
        final BidManagerView view = bidManagerFacade.getUserViewByRfpId(rfpId, user);
        return response.finalizeResult(view, BidManagerViewViewModel.FACTORY);
    }
}