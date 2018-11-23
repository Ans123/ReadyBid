package net.readybid.bidmanagerview;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.entities.Id;
import net.readybid.rfp.core.Rfp;
import org.bson.types.ObjectId;

/**
 * Created by DejanK on 4/3/2017.
 *
 */
public interface BidManagerViewFactory {

    String createNameForRfpView(String rfpName);

    BidManagerView createBuyerHotelRfpView(String viewName, Rfp rfp, ObjectId ownerUserAccountId);

    BidManagerView createChainHotelRfpView(String viewName, Id rfpId, Id ownerAccountId);

    BidManagerView createDefaultView(EntityType accountType, ObjectId id);
}
