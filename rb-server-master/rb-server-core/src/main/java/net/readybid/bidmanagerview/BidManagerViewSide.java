package net.readybid.bidmanagerview;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.rfp.type.RfpType;

/**
 * Created by DejanK on 5/2/2017.
 *
 */
public enum BidManagerViewSide {
    BUYER, SUPPLIER;

    public static BidManagerViewSide determineSide(RfpType rfpType, EntityType accountType){
        return rfpType.isSupplier(accountType) ? SUPPLIER : BUYER;
    }
}
