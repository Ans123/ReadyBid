package net.readybid.bidmanagerview;

import java.util.List;

/**
 * Created by DejanK on 5/19/2017.
 *
 */
public interface BidManagerViewCreationRepository {

    BidManagerView createViewIfDoesNotExist(BidManagerView view);

    void createViews(List<BidManagerView> views);
}
