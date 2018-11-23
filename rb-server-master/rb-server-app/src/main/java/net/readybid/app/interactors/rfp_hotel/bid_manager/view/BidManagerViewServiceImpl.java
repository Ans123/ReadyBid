package net.readybid.app.interactors.rfp_hotel.bid_manager.view;

import net.readybid.auth.user.AuthenticatedUser;
import net.readybid.bidmanagerview.BidManagerView;
import net.readybid.bidmanagerview.BidManagerViewFactory;
import net.readybid.bidmanagerview.BidManagerViewListItemViewModel;
import net.readybid.rfp.core.Rfp;
import net.readybid.utils.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DejanK on 3/31/2017.
 *
 */
@Service
public class BidManagerViewServiceImpl implements BidManagerViewService {

    private final BidManagerViewFactory bidManagerViewFactory;
    private final BidManagerViewRepository bidManagerViewRepository;

    @Autowired
    public BidManagerViewServiceImpl(BidManagerViewFactory bidManagerViewFactory, BidManagerViewRepository bidManagerViewRepository) {
        this.bidManagerViewFactory = bidManagerViewFactory;
        this.bidManagerViewRepository = bidManagerViewRepository;
    }

    @Override
    public void createRfpView(Rfp rfp, AuthenticatedUser user) {
        final String viewName = generateViewName(rfp.getName(), user);
        final BidManagerView view = bidManagerViewFactory.createBuyerHotelRfpView(viewName, rfp, user.getAccountId());
        bidManagerViewRepository.createView(view);
    }

    @Override
    public void updateRfpViewName(String rfpId, String name, AuthenticatedUser user) {
        final String viewName = generateViewName(name, user);
        bidManagerViewRepository.updateViewName(rfpId, viewName);
    }

    @Override
    public BidManagerView getUserView(String viewId, AuthenticatedUser user) {
        return bidManagerViewRepository.findViewById(viewId, user);
    }

    @Override
    public BidManagerView getUserViewByRfpId(String rfpId, AuthenticatedUser user) {
        return bidManagerViewRepository.findViewByRfpIdAndOwner(rfpId, user);
    }

    @Override
    public ListResult<BidManagerViewListItemViewModel> listUserViews(AuthenticatedUser user) {
        return bidManagerViewRepository.listViews(user);
    }

    private String generateViewName(String rfpName, AuthenticatedUser user) {
        final String defaultViewName = bidManagerViewFactory.createNameForRfpView(rfpName);
        String viewName = defaultViewName;
        int counter = 0;
        String format = "%s (%d)";
        while(null != bidManagerViewRepository.findViewByNameForUser(viewName, user)){
            counter++;
            viewName = String.format(format, defaultViewName, counter);
        }
        return viewName;
    }
}
