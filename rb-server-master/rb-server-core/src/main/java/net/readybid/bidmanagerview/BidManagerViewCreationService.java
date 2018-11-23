package net.readybid.bidmanagerview;

import net.readybid.app.core.entities.entity.EntityType;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by DejanK on 5/19/2017.
 *
 */
public interface BidManagerViewCreationService {
    BidManagerView createDefaultViewsForUser(EntityType accountType, ObjectId userAccountId);

    void createChainRepRfpViews(List<HotelRfpCreateChainRfpBidManagerViewCommand> commands);
}
