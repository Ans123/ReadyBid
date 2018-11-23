package net.readybid.app.persistence.mongodb.repository;

import net.readybid.app.interactors.rfp_hotel.bid.implementation.supplier.HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl;
import org.bson.conversions.Bson;

import java.util.List;

public interface HotelRfpBidSetBidContactDataRepository {
    List<HotelRfpBidSetSupplierContactAndSendBidCommandProducerImpl> aggregate(List<? extends Bson> pipeline);
}
