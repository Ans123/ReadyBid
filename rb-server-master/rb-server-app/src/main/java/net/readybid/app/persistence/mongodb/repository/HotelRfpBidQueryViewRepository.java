package net.readybid.app.persistence.mongodb.repository;

import net.readybid.app.entities.rfp_hotel.bid.HotelRfpBidQueryView;
import org.bson.conversions.Bson;

import java.util.List;

public interface HotelRfpBidQueryViewRepository {

    List<HotelRfpBidQueryView.Builder> findAll(Bson filter, Bson projection);
}
