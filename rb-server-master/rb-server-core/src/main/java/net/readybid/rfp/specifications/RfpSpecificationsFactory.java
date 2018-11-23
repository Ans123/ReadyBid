package net.readybid.rfp.specifications;

import net.readybid.rfp.buyer.Buyer;
import net.readybid.rfp.type.RfpType;

/**
 * Created by DejanK on 1/7/2017.
 *
 */
public interface RfpSpecificationsFactory {

    RfpSpecifications createSpecifications(RfpType rfpType, Buyer buyerCompanyName);
}
