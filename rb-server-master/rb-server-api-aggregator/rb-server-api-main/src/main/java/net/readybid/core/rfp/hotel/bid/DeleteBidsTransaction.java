package net.readybid.core.rfp.hotel.bid;

import net.readybid.app.entities.rfp_hotel.dirty.DeleteBidRequest;
import net.readybid.app.entities.rfp_hotel.dirty.DeleteBidsResult;

public interface DeleteBidsTransaction {

    DeleteBidsResult delete(DeleteBidRequest request);
}
