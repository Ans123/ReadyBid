package net.readybid.rfp.core;

import org.bson.types.ObjectId;

/**
 * Created by DejanK on 9/14/2016.
 *
 */
public interface HotelRfpBuyerCompanyAndProgramYear {

    ObjectId getBuyerCompanyAccountId();

    int getProgramYear();
}
