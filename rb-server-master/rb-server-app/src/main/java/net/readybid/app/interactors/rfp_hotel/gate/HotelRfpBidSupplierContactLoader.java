package net.readybid.app.interactors.rfp_hotel.gate;

import net.readybid.app.core.entities.entity.EntityType;
import net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier.HotelRfpBidSetSupplierContactAndSendBidCommandProducer;

import java.util.List;

public interface HotelRfpBidSupplierContactLoader {

    String getEntityId(EntityType type, List<String> bidsIds);

    List<HotelRfpBidSetSupplierContactAndSendBidCommandProducer> loadSetBidContactData(List<String> bidsIds);
}
